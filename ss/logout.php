<?php 
	session_start();

	$d="";
	 
	if(isset($_SESSION['uid']))
	{
		$auth = $_SESSION['uid'];
		$document = file_get_contents("members.txt");
		$lines = explode("\n", $document);

		$myfile = fopen("members.txt", "w") or die("Unable to open file!");
		foreach ($lines as $newline) 
		{
			$tokens = explode("|", $newline);

			if(strcmp($auth, $tokens[0])===0)
			{
				if(isset($_SESSION['a1'])) {	$tokens[6] 	+= $_SESSION['a1']; 	}
				if(isset($_SESSION['a2'])) {	$tokens[7] 	+= $_SESSION['a2']; 	}
				if(isset($_SESSION['s1'])) {	$tokens[8] 	+= $_SESSION['s1'];		}
				if(isset($_SESSION['s2'])) {	$tokens[9] 	+= $_SESSION['s2'];		}
				if(isset($_SESSION['m1'])) {	$tokens[10] += $_SESSION['m1'];		}
				if(isset($_SESSION['m2'])) {	$tokens[11] += $_SESSION['m2'];		}
				if(isset($_SESSION['d1'])) {	$tokens[12] += $_SESSION['d1'];		}
				if(isset($_SESSION['d2'])) {	$tokens[13] += $_SESSION['d2'];		}

				$tokens[14] = $tokens[6] + $tokens[8] + $tokens[10] + $tokens[12];
				$tokens[15] = $tokens[7] + $tokens[9] + $tokens[11] + $tokens[13];

				$d = implode("|", $tokens);
				$memberInsert = $d."\n";
				fwrite($myfile, $memberInsert);	
			}
			else{
				$d = implode("|", $tokens);
				$memberInsert = $d."\n";
				fwrite($myfile, $memberInsert);	
			}
		}
		fclose($myfile);
  	}
    unset($_SESSION['uid']);
	session_destroy();
  ?>

  <?php include("includes/header.php") //common header ?> 

				<li><a href="login.php">Login</a></li>
		    </ul>
		</div>

		<div id="section">
	<h1>Logout Page</h1>
	<p>Please click here to <a href="login.php">login</a></p>

<?php include("includes/footer.php") //common footer ?> 
