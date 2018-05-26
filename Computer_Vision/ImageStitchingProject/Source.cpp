#include "image_matcher.h"

#include <string>
#include <iostream>


Mat setImage(String imageName)
{
	Mat image;
	String imagePath("input/" + imageName + ".png");
	Mat img = imread(imagePath, CV_LOAD_IMAGE_GRAYSCALE);

	if (img.empty())			// Check for invalid input
	{
		cout << "Could not open or find the image" << endl;
		return img;
	}

	Mat grayImg;
	if (image.channels() != 1)
	{
		cvtColor(img, grayImg, CV_BGR2GRAY);
	}
	else
	{
		grayImg = img;
	}

	grayImg.convertTo(image, CV_32FC1, 1.0 / 255, 0);
	return img;
}

int main() {

	//Mat image1 = imread("input/Rainier1.png", CV_LOAD_IMAGE_GRAYSCALE);
	//Mat image2 = imread("input/Rainier2.png", CV_LOAD_IMAGE_GRAYSCALE);

	Mat image1 = imread("input/ND1.png", CV_LOAD_IMAGE_GRAYSCALE);
	Mat image2 = imread("input/ND2.png", CV_LOAD_IMAGE_GRAYSCALE);

	if (image1.empty() || image2.empty())
	{
		printf("Can't read one of the images\n");
		return -1;
	}

	image1.convertTo(image1, CV_8UC1);
	image2.convertTo(image2, CV_8UC1);

	int imgtype1 = image1.type();
	int imgtype2 = image2.type();

	cout << "Image1 type " << CV_MAT_DEPTH(imgtype1) << " , " << CV_MAT_CN(imgtype1) << endl;
	cout << "Image2 type " << CV_MAT_DEPTH(imgtype2) << " , " << CV_MAT_CN(imgtype2) << endl;

	//for (int i = 0; i < image1.cols; ++i) 
	//{
	//	for (int j = 0; j < image1.rows; ++j)
	//	{
	//		cout << (int) image1.at<uchar>(j, i) << " , ";
	//	}
	//	cout << endl;
	//}
	//image1.convertTo(image1, CV_32FC1, 1.0 / 255, 0);
	//image2.convertTo(image2, CV_32FC1, 1.0 / 255, 0);

	//// Convert color to grayscale
	//Mat img1, img2;
	//cvtColor(image1, img1, COLOR_BGR2GRAY);
	//cvtColor(image2, img2, COLOR_BGR2GRAY);

	//// Convert it to CV_8U3 Mat

	//img1.convertTo(image1, CV_8U, 1, 0); // CV_8U should work as well
	//img2.convertTo(image2, CV_8U, 1, 0); // CV_8U should work as well

	//imshow("ND1", image1);
	//imshow("ND2", image2);

	imgtype1 = image1.type();
	imgtype2 = image2.type();

	cout << "Image1 type " << CV_MAT_DEPTH(imgtype1) << " , " << CV_MAT_CN(imgtype1) << endl;
	cout << "Image2 type " << CV_MAT_DEPTH(imgtype2) << " , " << CV_MAT_CN(imgtype2) << endl;

	vector<DMatch> matches;
	vector<KeyPoint> keyPoints1, keyPoints2;

	ImageMatcher imgMatcher(0.8f, 0.98, 1.0, 10);
	
	 Mat F = imgMatcher.match(image1, image2, matches, keyPoints1, keyPoints2);
	// Mat F = imgMatcher.match(img1, img2, matches, keyPoints1, keyPoints2);
	waitKey(0);
	return 0;
}