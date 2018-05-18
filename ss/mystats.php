<?php
	session_start();
	if(!isset($_SESSION['uid']))
	{	
		$stat = 'Login';
		$link = 'login.php';
   		header("Location: login.php");
	}
	else
	{	$link = 'logout.php';
		$stat = 'Logout';

		$_SESSION['a1']=((isset($_SESSION['a1']))?$_SESSION['a1']:0);
		$_SESSION['a2']=((isset($_SESSION['a2']))?$_SESSION['a2']:0);

		$_SESSION['s1']=((isset($_SESSION['s1']))?$_SESSION['s1']:0);
		$_SESSION['s2']=((isset($_SESSION['s2']))?$_SESSION['s2']:0);

		$_SESSION['m1']=((isset($_SESSION['m1']))?$_SESSION['m1']:0);
		$_SESSION['m2']=((isset($_SESSION['m2']))?$_SESSION['m2']:0);	

		$_SESSION['d1']=((isset($_SESSION['d1']))?$_SESSION['d1']:0);
		$_SESSION['d2']=((isset($_SESSION['d2']))?$_SESSION['d2']:0);	

		$_SESSION['numAddProbs'] = (isset($_SESSION['numAddProbs'])?$_SESSION['numAddProbs'] + $_SESSION['a1']:$_SESSION['a1']);
		$_SESSION['addScore'] = (isset($_SESSION['addScore'])?$_SESSION['addScore'] + $_SESSION['a2']: $_SESSION['a2']);
		
		$_SESSION['numSubProbs'] = (isset($_SESSION['numSubProbs'])?$_SESSION['numSubProbs'] + $_SESSION['s1']:$_SESSION['s1']);
		$_SESSION['subScore'] = (isset($_SESSION['subScore'])?$_SESSION['subScore'] + $_SESSION['s2']:$_SESSION['s2']);
		
		$_SESSION['numMultProbs'] = (isset($_SESSION['numMultProbs'])?$_SESSION['numMultProbs'] + $_SESSION['m1']:$_SESSION['m1']);
		$_SESSION['multScore'] = (isset($_SESSION['multScore'])?$_SESSION['multScore'] + $_SESSION['m2']:$_SESSION['m2']);

		$_SESSION['numDivProbs'] = (isset($_SESSION['numDivProbs'])?$_SESSION['numDivProbs'] + $_SESSION['d1']:$_SESSION['d1']);
		$_SESSION['divScore'] = (isset($_SESSION['divScore'])?$_SESSION['divScore'] + $_SESSION['d2']:$_SESSION['d2']);
	}

		$Lines = file("members.txt");
		$auth = $_SESSION['uid'];

		$da=array();
		foreach($Lines as $Key => $Val) 
		{ 
			$Data[$Key] = explode("|", $Val);
			if (($Data[$Key][0]) == $auth ) 
			{
				for($i=0;$i<=9;$i++)
				{
					$da[$i] = $Data[$Key][6+$i] ;
				}
				
			}
		}
 ?>
 <?php include("includes/header.php") //common header ?>
		      	<li><a href="<?php print $link;?>"><?php echo $stat; ?></a></li>
	        </ul>
		</div>

		<div id="section">
			<h1>My Statistics</h1>
				<table>
					<thead> 
					<tr><th colspan="3" align="center">Current Session Statistics</th></tr>
					<tr>
					<th>Type</th>
					<th>Done</th>
					<th>Score</th>
					</tr>
					</thead>
					<tbody>
					<tr>
					<td>Addition</td>
					<td><?php echo $_SESSION['numAddProbs'];?></td>
					<td><?php echo $_SESSION['addScore'];?></td>
					</tr>
					<tr>
					<td>Subtraction</td>
					<td><?php echo $_SESSION['numSubProbs'];?></td>
					<td><?php echo $_SESSION['subScore'];?></td>
					</tr>
					<tr>
					<td>Multiplication</td>
					<td><?php echo $_SESSION['numMultProbs'];?></td>
					<td><?php echo $_SESSION['multScore'];?></td>
					</tr>
					<tr>
					<td>Division</td>
					<td><?php echo $_SESSION['numDivProbs'];?></td>
					<td><?php echo $_SESSION['divScore'];?></td>
					</tr>
					</tbody>
					<tfoot>
					<tr>
					<td>Total</td>
					<td><?php echo ($_SESSION['numAddProbs']+$_SESSION['numSubProbs'] + 
					$_SESSION['numMultProbs'] + $_SESSION['numDivProbs']);?></td>
					<td><?php echo ($_SESSION['addScore']+$_SESSION['subScore'] + 
					$_SESSION['multScore'] + $_SESSION['divScore']);?></td>
					</tr>
					</tfoot>
				</table>

				<br/>
				<br/>

				<table>
					<thead>
					<tr>
					<tr><th colspan="3" align="center"> File Statistics </th></tr>
					<th>Type</th>
					<th>Done</th>
					<th>Score</th>
					</tr>
					</thead>
					<tbody>
					<tr>
					<td>Addition</td>
					<td><?php echo $da[0];?></td>
					<td><?php echo $da[1];?></td>
					</tr>
					<tr>
					<td>Subtraction</td>
					<td><?php echo $da[2];?></td>
					<td><?php echo $da[3];?></td>
					</tr>
					<tr>
					<td>Multiplication</td>
					<td><?php echo $da[4];?></td>
					<td><?php echo $da[5];?></td>
					</tr>
					<tr>
					<td>Division</td>
					<td><?php echo $da[6];?></td>
					<td><?php echo $da[7];?></td>
					</tr>
					</tbody>
					<tfoot>
					<tr>
					<td>Total</td>
					<td><?php echo $da[8];?></td>
					<td><?php echo $da[9];?></td>
					</tr>
					</tfoot>
				</table>
	  
<?php include("includes/footer.php") //common footer ?> 

