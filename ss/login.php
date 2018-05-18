<?php session_start(); ?>
<?php include("includes/header.php") //common header ?>

		      	<li><a href="logout.php">Logout</a></li>
	        </ul>
		</div>

		<div id="section">
			<?php require_once("validate_loginForm.php"); ?>

	<h1>Login</h1>
	<form name="register" id="logForm" onsubmit="" method="post" action="<?php echo htmlspecialchars($_SERVER["PHP_SELF"]);?>" novalidate>
		<span class="form_status"><h2>
			<?php echo $form_status;
				if($form_status == "Login Successful!") {
					echo "<br/> Let's go solving ... redirecting";
					header( "refresh:5; url=index.php" );
				}
			?>

	    <fieldset>
            <legend>Login Information</legend>

			<div class="left_align"> Login Name</div>
			<input type = "text" id="loginName" name="loginName" value="<?php if(isset($_POST['loginName'])) echo $_POST['loginName']; ?>" autofocus/>
			<span class="error">* <?php echo $lgnameErr;?></span>
			<br >

			<div class="left_align"> Password</div>
			<input type = "password" id="passwrd1" name="passwrd" value="<?php if(isset($_POST['passwrd'])) echo $_POST['passwrd']; ?>"/>
			<span class="error">* <?php echo $passwErr;?></span>
			<br >
			
			<input type = "submit" value="Login" name="submit"/>
			<br >
			<br >
			<span> <a href="Register.php">Register</a> </span> 
			<br >
			<br >
			<span class="error">* required fields.</span>
		</fieldset>
	</form>

<?php include("includes/footer.php") //common footer ?> 