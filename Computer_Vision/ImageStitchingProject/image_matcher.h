#include <opencv2/opencv.hpp>
#include <opencv2/core/core.hpp>
#include <opencv2/calib3d.hpp>
#include <opencv2/ccalib.hpp>
#include <opencv2/highgui/highgui.hpp>
#include <opencv2/features2d.hpp>
#include <opencv2/xfeatures2d.hpp>
#include <opencv2/xfeatures2d/nonfree.hpp>
#include <opencv2/imgproc/imgproc.hpp>

using namespace std;
using namespace cv;
using namespace cv::xfeatures2d; 

class ImageMatcher
{

private:
	Ptr<Feature2D> detector;
	//Ptr<Feature2D> extractor;
	float ratio;			// max ratio between first and second NN
	double minDist;			// min distance to epipolar
	double confiLevel;

public:
	ImageMatcher(float r, double d, double c, int keyPtsMaxNum) 
		: ratio(0.65f), confiLevel(0.98), minDist(3.0)
	{
		ratio = r;
		minDist = d;
		confiLevel = c;
		detector = xfeatures2d::SIFT::create();
		//extractor = SIFT::create(100);
	}

	// Match feature points using symmetry test and RANSAC
	Mat match(
		Mat& image1, Mat& image2, 
		vector<DMatch>& matches, 
		vector<KeyPoint> keyPts1, vector<KeyPoint> keyPts2);

	// clear matches for which NN ratio is > threshold
	// return the number of removed points
	// corresponding removed entries will have size 0
	int ratioTest(vector<vector<DMatch>> &matches);

	// accepts a match pair if both are best matches to each other (symmetrical)
	void symmetryTest(
		const vector<vector<DMatch>>& matches1,  
		const vector<vector<DMatch>>& matches2,
		vector<DMatch>& symMatches);

	// convert keypoints into Point2f
	void convertToPoint2f(
		const vector<DMatch>& matches,
		const vector<KeyPoint>& keyPts1, 
		const vector<KeyPoint>& keyPts2,
		vector<Point2f>& pts1,
		vector<Point2f>& pts2);

	// extract good matches by RANSAC
	// return the fundamental matrix
	Mat ransacTest(
		const vector<DMatch>& matches,
		const vector<KeyPoint>& pts1,
		const vector<KeyPoint>& pts2,
		vector<DMatch>& outMatches);
};