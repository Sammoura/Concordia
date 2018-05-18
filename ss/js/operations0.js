/*Java Script operations */

var userChoice 	= document.getElementsByName("choice");
var operand1 	= document.getElementById("op1");
var operand2 	= document.getElementById("op2");
var operation   = document.getElementById("op");
var res 		= document.getElementById("result");
var message1 	= document.getElementById("feed");
var message2 	= document.getElementById("score");
var num1, num2, numRes;
var numOftrials	= 0;
var problemOver = true;

numAddProbs = 0;
numSubProbs = 0;
numMultProbs= 0;

addScore = 0;
subScore = 0;
multScore = 0;

operationType;

// Returns a random integer between min (included) and max (excluded)
function getRandomInt(min, max) {
  return Math.floor(Math.random() * (max - min)) + min;
}

// Creates operands
function generateOperands(numDigits){

	switch (numDigits){
		case(1):
		num1 = getRandomInt(0,100);
		num2 = getRandomInt(0,100);
		break;

		case(2):
		num1 = getRandomInt(0,1000);
		num2 = getRandomInt(0,1000);
		break;

		default:
		num1 = getRandomInt(0,10);
		num2 = getRandomInt(0,10);
	}
}

// Creates a new Problem
function createProblem(opType){

	if(!problemOver){
		alert("You have to solve the current problem first");
		return;
	}

	switch(opType){
		case (1):
		numAddProbs++;
		break;

		case (2):
		numSubProbs++;
		break;

		case (3):
		numMultProbs++;
		break;
	}

	problemOver=false;
	res.value = "";
	message1.value="";
	numOftrials=0;
	var numDigits;
	for(var i = 0; i < userChoice.length; i++){
		if(userChoice[i].checked){
			numDigits = i;
		}
	}

	var limit = Math.pow(10,numDigits+1);

	generateOperands(numDigits);
	switch(opType){
		case(1):
		operation.value="+";
		numRes = num1+num2;
		break;

		case(2):
		operation.value="-";
		if(num1 < num2){			//swap
			num1 = num1 + num2;
			num2 = num1 - num2;
			num1 = num1 - num2;
		};
		numRes = num1-num2;
		break;

		case(3):
		operation.value="×";
		numRes = num1*num2;
		break;
	}
	operand1.value = num1;
	operand2.value = num2;
	res.focus();
}

function createMix(){
	operationType = getRandomInt(0,3);
	operationType = operationType%3+1;
	createProblem(operationType);
}

function checkMix(){
	check(operationType);
}

function check(opType){
	if(problemOver){
		alert("You have already finished this one");
		return;
	}
	
	var userAns = parseInt(res.value);

	message1.value="Trial number: "+(++numOftrials);

	if(numOftrials > 3){
		alert("Sorry no more trials are left for this problem \nThe correct answer is: " + numRes);
		problemOver = true;
		return;
	}

	if(isNaN(userAns) || userAns===""){
		alert("Please write a number");
		res.value = "";
		return;
	}

	if(userAns === numRes){
		switch(opType){
			case (1):
			addScore++;
			break;

			case (2):
			subScore++;
			break;

			case (3):
			multScore++;
			break;
		}

		problemOver = true;

		switch(numOftrials){
		case(1):
		message1.value = "Excellent \n\nyou got it on the first trial !!!";
		break;

		case(2):
		message1.value = "VeryGood \n\nYou got it on the second trial !!";
		break;

		case(3):
		message1.value = "Good \n\nYou got it on the third trial !";
		break;
		}
	}
	else{
		message1.value = "Sorry, wrong answer \n\nTrial number: "+(numOftrials);
	}
		switch(opType){
			case (1):
			message2.value = "Your Addition score: \n\n" + addScore + " correct out of " + numAddProbs;
			break;

			case (2):
			message2.value = "Your Subtraction score: \n\n" + subScore + " correct out of " + numSubProbs;
			break;

			case (3):
			message2.value = "Your Multiplication score: \n\n" + multScore + " correct out of " + numMultProbs;
			break;
		}		
}