<?php
	if($_SERVER['REQUEST_METHOD'] == 'POST'){		
		$gender = $_POST['gender'];
		$weight = (int)$_POST['weight'];
		
		$weightD = $weight; 
		
		$jsonReturn = array();
		$jsonReturn['success'] = "0";
		$jsonReturn['bp'] = array();
		$jsonReturn['squat'] = array();
		$jsonReturn['deadlift'] = array();
		
		require_once 'connect.php';
		
		$n1 = ($weightD)%10;
		$n2 = (int)($weightD/5);
			
		if(($n2 % 2) == 0){
			$weightD = $weight - $n1;
		}			
		else $weightD = ((int)($weight/10))*10+ 5;
		
		//BP		
		if(strcasecmp($gender, 'male') == 0){
			$sql = "SELECT * FROM male_benchpress_standards WHERE Bodyweight = '$weightD'";
		}
		else if(strcasecmp($gender, 'female') == 0){
			$sql = "SELECT * FROM female_benchpress_standards WHERE Bodyweight = '$weightD'";
		}				
			
		$response = mysqli_query($conn, $sql);
		if($response->num_rows == 1){
			$row = mysqli_fetch_assoc($response);
				
			$index['beginner'] = $row['Beginner'];
			$index['novice'] = $row['Novice'];
			$index['intermediate'] = $row['Intermediate'];
			$index['advanced'] = $row['Advanced'];
			$index['elite'] = $row['Elite'];
			
			array_push($jsonReturn['bp'], $index); 
		}
		
		//Squat
		if(strcasecmp($gender, 'male') == 0){
			$sql = "SELECT * FROM male_squat_standards WHERE Bodyweight = '$weightD'";
		}
		else if(strcasecmp($gender, 'female') == 0){
			$sql = "SELECT * FROM female_squat_standards WHERE Bodyweight = '$weightD'";
		}				
			
		$response = mysqli_query($conn, $sql);
		if($response->num_rows == 1){
			$row = mysqli_fetch_assoc($response);
				
			$index['beginner'] = $row['Beginner'];
			$index['novice'] = $row['Novice'];
			$index['intermediate'] = $row['Intermediate'];
			$index['advanced'] = $row['Advanced'];
			$index['elite'] = $row['Elite'];
			
			array_push($jsonReturn['squat'], $index); 
		}
		
		//Deadlift
		if(strcasecmp($gender, 'male') == 0){
			$sql = "SELECT * FROM male_deadlift_standards WHERE Bodyweight = '$weightD'";
		}
		else if(strcasecmp($gender, 'female') == 0){
			$sql = "SELECT * FROM female_deadlift_standards WHERE Bodyweight = '$weightD'";
		}				
			
		$response = mysqli_query($conn, $sql);
		if($response->num_rows == 1){
			$row = mysqli_fetch_assoc($response);
				
			$index['beginner'] = $row['Beginner'];
			$index['novice'] = $row['Novice'];
			$index['intermediate'] = $row['Intermediate'];
			$index['advanced'] = $row['Advanced'];
			$index['elite'] = $row['Elite'];
			
			array_push($jsonReturn['deadlift'], $index); 
		}
		
		if($jsonReturn['bp'] != null && $jsonReturn['squat'] != null && $jsonReturn['deadlift'] != null) $jsonReturn['success'] = "1";
		
		echo json_encode($jsonReturn);
		
		mysqli_close($conn);
	
	}
?>