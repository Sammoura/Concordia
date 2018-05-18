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
			$_SESSION['d1'] = $_GET['arguments']['div1'];
			$_SESSION['d2'] = $_GET['arguments']['div2'];	
		}	
	} 
?>
	<script type="text/javascript">
		function aj(num) {	
		check(num);
        arguments0 = 
        {
	        div1: divScore,
	        div2: numDivProbs
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
			<h1> Hello Division </h1>
			<form name="form4" class="operation_form">
				<fieldset>
					<legend>Division</legend>
					<label> Choose the number of digits </label>
					<!-- <input type="radio" name="choice" value="one" checked /> 0-9 	
					<input type="radio" name="choice" value="two"  /> 0-99 	            
					<input type="radio" name="choice" value="three"  /> 0-999	 -->           
					<br /><br />
					
					<table>
						<tr>
							<td> Min </td>
							<td>
								<!-- <br>
								<input type='button' value='-' class='qtyminus' field='choice_op1' /><br>
								<input type='text' name='choice_op1' value='0' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_op1' /> -->
							</td>
							<td></td>
							<td>
								<br>
								<input type='button' value='-' class='qtyminus' field='choice_op2_min' /><br>
								<input type='text' name='choice_op2_min' value='1' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_op2_min' />
							</td>
							<td>
								<br>
								<input type='button' value='-' class='qtyminus' field='choice_res_min' /><br>
								<input type='text' name='choice_res_min' value='0' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_res_min' />
							</td>
						</tr>
						
						<tr>
							<td>
								<input type="button" id="go" value="New" onclick="createProblem(4)" />
							</td>
							<td>
								<input type="text" id="op1"  readonly />
							</td>
							<td>
								<input type="text" id="op"  readonly />
							</td>
							<td>
								<input type="text" id="op2"  readonly />
							</td>
							<td>
								= <input type="text" id="result" /> 
							</td>
						</tr>

						<tr>
							<td> Max </td>
							<td>
								<!-- <br>
								<input type='button' value='-' class='qtyminus' field='choice_op1' /><br>
								<input type='text' name='choice_op1' value='0' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_op1' /> -->
							</td>
							<td></td>
							<td>
								<br>
								<input type='button' value='-' class='qtyminus' field='choice_op2_max' /><br>
								<input type='text' name='choice_op2_max' value='1' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_op2_max' />
							</td>
							<td>
								<br>
								<input type='button' value='-' class='qtyminus' field='choice_res_max' /><br>
								<input type='text' name='choice_res_max' value='0' class='qty' /><br>
								<input type='button' value='+' class='qtyplus' field='choice_res_max' />
							</td>
						</tr>
					</table>	
					<input type="button" id="see" value="Check" onclick="aj(4)" />				<br /><br />

					<textarea id="feed" cols="40" rows = "4" readonly> </textarea>
					<textarea id="score" cols="40" rows = "4" readonly> </textarea>
				</fieldset>
				<script type="text/javascript" src="js/operations11.js"></script>
			</form>

<?php include("includes/footer.php") //common footer ?> 