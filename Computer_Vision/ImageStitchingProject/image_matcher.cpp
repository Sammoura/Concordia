#include "image_matcher.h"

// matches feature points using symmetry test and RANSAC
// returns the fundamental matrix
Mat ImageMatcher::match(Mat& img1, Mat& img2, 
	vector<DMatch>& matches, vector<KeyPoint> keyPts1, vector<KeyPoint> keyPts2	) 
{
	// [1] Feature Detection (detect feature points)
	cout << "[1] Feature detection " << endl;
	detector->detect(img1, keyPts1);
	detector->detect(img2, keyPts2);
	cout << keyPts1.size() << endl;
	cout << keyPts2.size() << endl;
	////-- Draw keypoints
	//Mat img_keypoints_1; Mat img_keypoints_2;
	//drawKeypoints(img1, keyPts1, img_keypoints_1, Scalar::all(-1), DrawMatchesFlags::DEFAULT);
	//drawKeypoints(img2, keyPts2, img_keypoints_2, Scalar::all(-1), DrawMatchesFlags::DEFAULT);
	////-- Show detected (drawn) keypoints
	//imshow("Keypoints 1", img_keypoints_1);
	//imshow("Keypoints 2", img_keypoints_2);

	// [2] Feature Extraction
	cout << "[2] Feature extraction " << endl;
	Mat descriptors1, descriptors2;
	detector->compute(img1, keyPts1, descriptors1);
	detector->compute(img2, keyPts2, descriptors2);

	// [3] Match descriptors
	cout << "[3] Match descriptors " << endl;
	BFMatcher matcher(NORM_L2);
	matcher.match(descriptors1, descriptors2, matches);

	// find the 2 best matching points for each feature in both directions img1 <--> img2
	// (a) for each feature point in img1, find the best 2 matches in img2	(knn with k = 2)
	vector<vector<DMatch>> matches1;
	matcher.knnMatch(descriptors1, descriptors2, matches1, 2);

	// (b) for each feature point in img2, find the best 2 matches in img1
	vector<vector<DMatch>> matches2;
	matcher.knnMatch(descriptors2, descriptors1, matches2, 2);

	// [4] refine matches by a threshold
	// remove matches for which NN ratio is > threshold
	cout << "[4] Refine matches " << endl;
	// clean img1 -> img2 matches
	int removed1 = ratioTest(matches1);

	// clean img2 -> img1 matches
	int removed2 = ratioTest(matches2);

	// [5] remove non symmetrical matches 
	cout << "[5] Remove non symmetrical matches " << endl;
	vector<DMatch> symMatches;
	symmetryTest(matches1, matches2, symMatches);

	Mat img_matches;
	drawMatches(img1, keyPts1, img2, keyPts2,
		matches, img_matches, Scalar::all(-1), Scalar::all(-1),
		vector<char>(), DrawMatchesFlags::NOT_DRAW_SINGLE_POINTS);

	//-- Show detected matches
	imshow("Good Matches", img_matches);
	imwrite("output/good_matches.png", img_matches);

	// [6] validate matches using RANSAC
	cout << "[6] RANSAC validation " << endl;
	Mat fundamental = ransacTest(symMatches, keyPts1, keyPts2, matches);

	return fundamental;
}

// clear matches for which NN ratio is > threshold
// return the number of removed points
// corresponding removed entries will have size 0
int ImageMatcher::ratioTest(vector<vector<DMatch>> &matches)
{
	int removedCtr = 0;
	for (vector<vector<DMatch>>::iterator it = matches.begin(); 
		it != matches.end(); ++it)
	{
		if (it->size() > 1)		// if NN = 2
		{
			// 1st best match distance : 2nd best match distance
			double r = (*it)[0].distance / (*it)[1].distance;
			// verify that r is not greater than the threshold, otherwise
			// both matches are relatively close in distance and there
			// is a high chance of selecting a wrong match, so remove both
			// r <= ratio indicates first match distance is relatively lower
			// than second best match and is accepted as a good match
			if (r > ratio)
			{
				it->clear();	// remove the match
				removedCtr++;
			}
		}
		else
		{
			it->clear();
			removedCtr++;
		}
	}
	return removedCtr++;
}

// accepts a match pair if both are best matches to each other (symmetrical)
// in both directions img1 <--> img2
void ImageMatcher::symmetryTest(
	const vector<vector<DMatch>>& matches1,
	const vector<vector<DMatch>>& matches2,
	vector<DMatch>& symMatches)
{
	// img1 --> img2
	for (vector<vector<DMatch>>::const_iterator it1 = matches1.begin();
		it1 != matches1.end(); ++it1)
	{
		if (it1->size() >= 2)
		{
			for (vector<vector<DMatch>>::const_iterator it2 = matches2.begin();
				it2 != matches1.end(); ++it2)
			{
				if (it2->size() >= 2)
				{
					// symmetrical matching
					if ( (*it1)[0].queryIdx == (*it2)[0].trainIdx &&
						 (*it1)[0].trainIdx == (*it2)[0].queryIdx )
					{
						symMatches.push_back(DMatch((*it1)[0].queryIdx,
							(*it1)[0].trainIdx, (*it1)[0].distance));
						break;
					}

				}
			}
		}
	}
}


// extract good matches by RANSAC
// return the fundamental matrix
Mat ImageMatcher::ransacTest(
	const vector<DMatch>& matches,
	const vector<KeyPoint>& keyPts1,
	const vector<KeyPoint>& keyPts2,
	vector<DMatch>& outMatches)
{
	// convert keyPts into Point2f
	vector<Point2f> pts1, pts2;
	for (vector<DMatch>::const_iterator it = matches.begin();
		it != matches.end(); ++it)
	{
		pts1.push_back(Point2f(keyPts1[it->queryIdx].pt));
		pts2.push_back(Point2f(keyPts2[it->trainIdx].pt));
	}

	// compute F matrix using RANSAC
	vector<uchar> inliers(pts1.size(), 0);

	cout << " before findFundamentalMat " << endl;
	Mat fundamental = 
		findFundamentalMat(Mat(pts1), Mat(pts2), inliers, CV_FM_RANSAC, minDist, confiLevel );
	cout << " hereeeeeeeeeeeeeeeeeeeeeeeeeeee" << endl;
	
	// extrct the surviving matches
	vector<DMatch>::const_iterator it2 = matches.begin();
	for (vector<uchar>::const_iterator it1 = inliers.begin(); 
		it1 != inliers.end(); ++it1, ++it2)
	{
		if (*it1)
		{
			outMatches.push_back(*it2);
		}
	}

	pts1.clear();
	pts2.clear();
	for (vector<DMatch>::const_iterator it = outMatches.begin();
		it != matches.end(); ++it)
	{
		pts1.push_back(Point2f(keyPts1[it->queryIdx].pt));
		pts2.push_back(Point2f(keyPts2[it->trainIdx].pt));
	}

	// compute 8-point F from all survived matches
	if (false) 
	{
		fundamental = findFundamentalMat(Mat(pts1), Mat(pts2), CV_FM_8POINT);
	}

	return fundamental;
}