
<?php include("includes/header.php") //common header ?>
<?php
    if(!isset($_SESSION['uid']))
    {   
        $stat = 'Login';
        $link = 'login.php';
    }
    else
    {
        $stat = 'Logout';
        $link = 'logout.php';
    }
 ?>
                <li><a href="<?php print $link;?>"><?php echo $stat; ?></a></li>
            </ul>
        </div>

        <div id="section">
<?php require_once("validate_registerForm.php"); ?>

					<h1> Registeration page </h1>
            				<form id="regForm" onsubmit="vform()" method="post" name="regForm" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" novalidate>
                            <span class="form_status"><?php echo $form_status;?></span>
            					<fieldset>
            						<legend>Personal Information</legend>

                					<div class="left_align"> First Name</div>
                					<input name="First_Name" type="text" value="<?php if(isset($_POST['First_Name'])) echo $_POST['First_Name']; ?>" autofocus> 
                                    <span class="error">* <?php echo $First_NameErr;?></span> (Letters and hyphen only)
                                    <br>

                					<div class="left_align"> Last Name</div>
                					<input name="Last_Name" type="text" value="<?php if(isset($_POST['Last_Name'])) echo $_POST['Last_Name']; ?>"> 
                                    <span class="error">* <?php echo $Last_NameErr;?></span> (Letters and hyphen only)
                    				<br>

                    				<div class="left_align">Phone Number</div>
                					<input name="Phone_Number" type="text" value="<?php if(isset($_POST['Phone_Number'])) echo $_POST['Phone_Number']; ?>" >
                                    <span class="error">* <?php echo $Phone_NumberErr;?></span>
                					<br>
                                    
                    				<div class="left_align"> Email</div>
                					<input name="Email" type="text" value="<?php if(isset($_POST['Email'])) echo $_POST['Email']; ?>" >
                                    <span class="error">* <?php echo $EmailErr;?></span>
                    				<br>

                    			</fieldset>
                    			<fieldset> 
                    				<legend>Login Information</legend>

                    				<div class="left_align"> Login Name</div>
                					<input name="Login_Name" type="text" value="<?php if(isset($_POST['Login_Name'])) echo $_POST['Login_Name']; ?>" > 
                                    <span class="error">* <?php echo $Login_NameErr;?></span> (Letters and digits only)
                    				<br> 

                                    <br>
                    				<label> Your password can contain letters and digits and must contain at least one of each and is case sensitive </label> <br>
                    			    <div class="left_align"> Password</div>
                					<input name="Password" type="password" value="<?php if(isset($_POST['Password'])) echo $_POST['Password']; ?>" >
                                    <span class="error">* <?php echo $PasswordErr;?></span>
                    				<br>
                			
                    			    <div class="left_align"> Re-inter Password</div>
                					<input name="Password_Confirmation" type="password" value="<?php if(isset($_POST['Password_Confirmation'])) echo $_POST['Password_Confirmation']; ?>" >
                    			   	<span class="error">* <?php echo $Password_ConfirmationErr;?></span>
                                    <br>

                                    <input class="submit-button" type="submit" value="Submit Form">
                                    <input id="spbut" type="button" value="reset" onClick="resetPostData(); this.form.reset();" />
                                    <br><br>
                                    <span class="error">* required fields.</span>
                                </fieldset>
                                <script type="text/javascript" src="js/fields.js"></script>
                            </form>
<script>
    /* JS to clear form */
    function resetPostData() { <?php clearForm() ?> }
</script>

<?php include("includes/footer.php") //common footer ?> 