<?php
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		$email = $_POST['email'];
		$password = $_POST['password'];
		
		require_once 'connect.php';
		
		$sql = "SELECT * FROM users_table WHERE email='$email';";

		$response = mysqli_query($conn, $sql);
		
		$jsonReturn = array();
		$jsonReturn['calendar'] = array();
		$jsonReturn['profile'] = array();
		$jsonReturn['measurements'] = array();
		$jsonReturn['login'] = array();
		$jsonReturn['success'] = "0";
		$jsonReturn['version'] = "0";
		
		$row = mysqli_fetch_assoc($response);
		if(password_verify($password, $row['password'])){
			$index['id'] = $row['id'];
			$index['name'] = $row['name'];
			$index['email'] = $row['email'];
			
			$jsonReturn['success'] = "1";
			array_push($jsonReturn['login'], $index);
		}
		else {}
		
		//Get app v
		if($jsonReturn['success'] == "1"){
			$sql = "SELECT CurrentVersion FROM app_info_table";
			$response = mysqli_query($conn, $sql);
			while($row = mysqli_fetch_assoc($response)){	
				$jsonReturn['version'] = $row['CurrentVersion'];
			}
		}
		
		//Get Calendar Datas
		if($jsonReturn['success'] == "1"){
			$id = $jsonReturn['login'][0]['id'];
			
			$sql = "SELECT * FROM calendar_table WHERE UserId='$id';";
			
			$response = mysqli_query($conn, $sql);
			
			if($response->num_rows > 0){
				while($row = mysqli_fetch_assoc($response)){
					$indexCalendar['TimeId'] = $row['TimeId'];
					$indexCalendar['Note'] = $row['Note'];
					
					array_push($jsonReturn['calendar'], $indexCalendar);
				}
			}
			else if($response->num_rows == 0){
				$sql = "INSERT INTO calendar_table (UserId) VALUES ('$id');";
				mysqli_query($conn, $sql);
			}
		}
		
		//Get User Profile
		if($jsonReturn['success'] == "1"){
			$sql = "SELECT * FROM userinfo_table WHERE UserId='$id'";
			$response = mysqli_query($conn, $sql);
			
			if($response->num_rows > 0){
				while($row = mysqli_fetch_assoc($response)){
					$indexProfile['gender'] = $row['gender'];
					$indexProfile['age'] = $row['age'];
					$indexProfile['weight'] = $row['weight'];
					
					array_push($jsonReturn['profile'], $indexProfile);
				}
			}
			else if($response->num_rows == 0){
				$sql = "INSERT INTO userinfo_table (UserId) VALUES ('$id');";
				mysqli_query($conn, $sql);
			}
		}
		
		//Get User Strength And Body Measurement
		if($jsonReturn['success'] == "1"){
			$sql = "SELECT * FROM measurements_table WHERE UserId='$id';";
			$response = mysqli_query($conn, $sql);
			
			if($response->num_rows > 0){
				while($row = mysqli_fetch_assoc($response)){
					$indexMeasurements['bodyJSON'] = $row['BodyJSON'];
					$indexMeasurements['strengthJSON'] = $row['StrengthJSON'];
					
					array_push($jsonReturn['measurements'], $indexMeasurements);
				}
			}
			//First Call
			else if($response->num_rows == 0){
				$sql = "INSERT INTO measurements_table(UserId) VALUES('$id');";
				mysqli_query($conn,$sql);
			}
		}
		
		
		echo json_encode($jsonReturn);
		mysqli_close($conn);
	}
?>