<?php
	$lgname = $passw = "";
	$lgnameErr = $passwErr = "";
	$form_status = "";

	$error_msg = "Login Name and Password mismatch!";
	$success_msg = "Login Successful!";
	$valid = "";

	if ($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$valid = true;
		// Validate presence
		if (empty($_POST['loginName']))
	    {
    		$lgnameErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
		}
	   	else
    	{
    		$lgname = $_POST['loginName'];
		}

	 	if (empty($_POST['passwrd']))
    	{
			$passwErr = "required";
			$valid = false;
			$form_status = $error_msg;
    	}
	   	else
    	{
    		$passw = $_POST['passwrd'];
    	}
    	// check if the user exists 
    	if( !CheckExistence( $lgname, $passw ) )	{
    		$valid = false;
    		$form_status = $error_msg;
    	}	
	}

	if($valid)
		{
			$form_status = $success_msg;
			$_SESSION['uid'] = $_POST['loginName'];
		}

	function CheckExistence($LogName, $LogPass){

		$document = file_get_contents("members.txt");
		$lines = explode("\n", $document);

		foreach ($lines as $newline) {

			$tokens = explode("|", $newline);

			if (empty($tokens[0]))
			{
				return false;
			}
			else if(strcmp($LogName, $tokens[0])==0  && strcmp($LogPass, $tokens[1])==0)
			{
				return true;
			}
		}
		return false;
	}
?>