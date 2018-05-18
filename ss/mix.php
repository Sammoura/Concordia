<?php
	session_start();
	if(!isset($_SESSION['uid']))
	{	
		$stat = 'Login';
		$link = 'login.php';
		header("Location: login.php");
	}

	else
	{	
		$link = 'logout.php';
		$stat = 'Logout';
		
		if(isset($_GET['arguments']))
		{
			$_SESSION['a1'] += $_GET['arguments']['arg1'];
			$_SESSION['a2'] += $_GET['arguments']['arg2'];
			$_SESSION['s1'] += $_GET['arguments']['sub1'];
			$_SESSION['s2'] += $_GET['arguments']['sub2'];
			$_SESSION['m1'] += $_GET['arguments']['mul1'];
			$_SESSION['m2'] += $_GET['arguments']['mul2'];	
		}
	} 
?>
	<script type="text/javascript">
		function aj() {	
		checkMix();
		arguments0 = 
		{
			arg1: addScore,
		    arg2: numAddProbs,
		   	sub1: subScore,
		    sub2: numSubProbs,
		    mul1: multScore,
		    mul2: numMultProbs
		};
	  	$.ajax({
	    type: "GET",
	    //url: "processAjax.php",
	    data: {arguments: arguments0} }); }
	</script>

		<?php include("includes/header.php") //common header ?>
		      	<li><a href="<?php print $link;?>"><?php echo $stat; ?></a></li>
	        </ul>
		</div>

		<div id="section">
					<h1> Hello Mixed Operations </h1>
					<form name="form4" class="operation_form">
						<fieldset>
    						<legend>Add, Subtract and Multiply</legend>
							<label> Please choose the number of digits </label>
							<input type="radio" name="choice" value="one" checked /> 0-9 	
							<input type="radio" name="choice" value="two"  /> 0-99 	            
							<input type="radio" name="choice" value="three"  /> 0-999	            <br /><br />
							
							<input type="button" id="go" value="New" onclick="createMix()" />

							<input type="text" id="op1"  readonly />
							<input type="text" id="op"  readonly />
							<input type="text" id="op2"  readonly /> = 
							<input type="text" id="result" /> 
								
							<input type="button" id="see" value="Check" onclick="aj()" />				<br /><br />

							<textarea id="feed" cols="40" rows = "4" readonly> </textarea>
							<textarea id="score" cols="40" rows = "4" readonly> </textarea>	
						</fieldset>
						<script type="text/javascript" src="js/operations1.js"></script>
					</form>

<?php include("includes/footer.php") //common footer ?> 