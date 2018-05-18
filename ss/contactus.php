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

					<h1> Contact Us </h1>

					<table>
						<tr> 
							<th> Name </th>
							<td> Samer Ayoub</td>
						</tr>
						<tr> 
							<th> Address </th>
							<td> 7815 Mountain Sights Avenue, Montreal, Quebec, Canada </td>
						</tr>
						<tr> 
							<th> Cellular Phone </th>
							<td> 01 438 402 5101 </td>
						</tr>
						<tr> 
							<th> Please email me at </th>
							<td> <a href="mailto:MyName@MySite.com?subject=testing">MyName@MySite.com</a> </td>
						</tr>
					</table>

<?php include("includes/footer.php") //common footer ?> 