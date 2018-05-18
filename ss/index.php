<?php session_start(); ?>
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
					<h1>Home Page</h1>
						<h3> Welcome to <span class="style1"> Future Primary School</span> website </h3>
						<p>
								Quiz Me is your free website to workout your brain on basic Arithmetic operations <br />
								It's easy, just <a href="register.html">register</a>, join us and practice <br />
							
							Practice
							<ul> 
								<li><a href="add.php">Addition</a></li>
								<li><a href="sub.php">Subtraction</a></li>
								<li><a href="mult.php">Multiplication</a></li>
								<li><a href="division.php">Division</a></li>
								<li><a href="mix.php">All</a></li>
							</ul>
						</p>

<?php include("includes/footer.php") //common footer ?> 