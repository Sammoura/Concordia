// Java Script 
//changing header background

	var imgBg = document.getElementById("header");
	imgBg.style.backgroundImage="url('http://princegeorge.ebusinessreport.com/images/QuizMe_reg.png')";

	// array of strings of url of images
	var imageArrray = 
	[
		"url('http://princegeorge.ebusinessreport.com/images/QuizMe_reg.png')"  ,
		"url('http://images.clipartpanda.com/math-clip-art-math-kids-278133.jpg')" ,
		"url('http://mathchat.files.wordpress.com/2011/05/math-is-cool1.jpg')"  , 
		"url('http://www.thestar.com/content/dam/thestar/opinion/editorials/2013/12/06/teach_ontario_kids_the_joy_of_math_editorial/math.jpg')" , 
		"url('http://cdn7.staztic.com/app/a/936/936931/quiz-me-832825-0-s-307x512.jpg')" ,
		"url('http://www.woboe.org/thomasedison/SiteAssets/Pages/ConnectedMathProgram/Connected%20Math%20Tips.jpg')",
		"url('https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/512/math.png')" , 
		"url('https://encrypted-tbn1.gstatic.com/images?q=tbn:ANd9GcTlfNqkECcOxfAlVB7zTyZoe7t5rkBvAJw4Muma8lkOMxFWpNuu')",
		"url('https://pbs.twimg.com/profile_images/1566488853/Quiz_Me_.jpg')"
	];

	var imageIndex = 1;

	function changeImage(){
		var str = imageArrray[imageIndex];
		imgBg.style.backgroundImage = str;
		imageIndex = imageIndex+1;
		imageIndex = imageIndex%(imageArrray.length);	//repeat
	}
	var intervalHandler = setInterval(changeImage,4000);

	var stop = 0;
	imgBg.onclick = function() 	// click to stop the image flow, click again to resume image flow
	{
		if (stop%2 === 0)	{
			clearInterval(intervalHandler);
		}
		else{
			setInterval(changeImage,4000);
		}
		stop=stop+1;	
	};
