<?php
// session_start();

	// define variables
	$First_NameErr = $Last_NameErr = $Phone_NumberErr = $EmailErr = $Login_NameErr = $PasswordErr = $Password_ConfirmationErr = "";
	$First_Name = $Last_Name = $Phone_Number = $Email = $Login_Name = $Password = $Password_Confirmation = "";

	$form_status = "";
	$error_msg = "Error while processing registration, please review input!";
	$success_msg = "Your registration was successful!";
	$valid = "";

	if ($_SERVER["REQUEST_METHOD"] == "POST")
	{
		$valid = true;
		// Validate presence
		if (empty($_POST['First_Name']))
	    {
    		$First_NameErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
		}
	   	else
    	{
    		$First_Name = $_POST['First_Name'];
		}

	 	if (empty($_POST['Last_Name']))
    	{
			$Last_NameErr = "required";
			$valid = false;
			$form_status = $error_msg;
    	}
	   	else
    	{
    		$Last_Name = $_POST['Last_Name'];
    	}
	   
		if (empty($_POST['Phone_Number']))
    	{
    		$Phone_NumberErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
    	}
	   	else
    	{
    		$Phone_Number = $_POST['Phone_Number'];
    	}

   		if (empty($_POST['Email']))
    	{
    		$EmailErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
    	}
    	else if (!filter_var($_POST['Email'], FILTER_VALIDATE_EMAIL))
        {
            $EmailErr = "Invalid Email address";
            $valid = false;
            $form_status = $error_msg;
        }
	   	else
    	{
    		$Email = $_POST['Email'];
    	}

	    if (empty($_POST['Login_Name']))
    	{
    		$Login_NameErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
    	}
	   	else if (DuplicateLoginName($_POST['Login_Name'])) {	// check for duplicate Login Name 
	   		$Login_NameErr = "Login Name Exists";
    		$valid = false;
    		$form_status = $error_msg;
	   	}
    	else {
    		$Login_Name = $_POST['Login_Name'];
    	}

	    if (empty($_POST['Password']))
    	{
    		$PasswordErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
    	}
    	elseif (strlen($_POST['Password'])<8)	    		// Validate password length
		{
			$PasswordErr = "at least 8 characters";
	    	$valid = false;
	    	$form_status = $error_msg;
		}
		else
    	{
    		$Password = $_POST['Password'];
    	}

	    if (empty($_POST['Password_Confirmation']))
    	{
    		$Password_ConfirmationErr = "required";
    		$valid = false;
    		$form_status = $error_msg;
    	}
    	elseif (strlen($_POST['Password_Confirmation'])<8)	    		// Validate password length
		{
			$Password_ConfirmationErr = "at least 8 characters";
	    	$valid = false;
	    	$form_status = $error_msg;
		}
		elseif(strcmp($_POST['Password'], $_POST['Password_Confirmation'])!==0)	// Validate passwords match	
		{
			$Password_ConfirmationErr =  "Passwords don't match";
			$valid = false;
	    	$form_status = $error_msg;
		}
	   	else
    	{
    		$Password_Confirmation = $_POST['Password_Confirmation'];
    	}		
	}

	if($valid)
		{
			$form_status = $success_msg;
			clearForm();
			$myfile = fopen("members.txt", "a") or die("Unable to open file!");
	
			$memberInsert = $Login_Name."|".$Password."|".$First_Name."|".$Last_Name."|".$Phone_Number."|".$Email."|"."0|"."0|"."0|"."0|"."0|"."0|"."0|"."0|"."\r\n";

			fwrite($myfile, $memberInsert);
			fclose($myfile);
		}

	function DuplicateLoginName($LogName){

		$document = file_get_contents("members.txt");
		$lines = explode("\n", $document);

		foreach ($lines as $newline) {
			$tokens = explode("|", $newline);

				if(strcmp($LogName, $tokens[0])===0)
				{
					return true;
				}
		}
		return false;
	}

	function clearForm(){
		$_POST['First_Name'] = $_POST['Last_Name'] = $_POST['Phone_Number'] = $_POST['Email'] = $_POST['Login_Name'] = $_POST['Password'] = $_POST['Password_Confirmation'] = "";
	}

?>
