<?php
	if($_SERVER['REQUEST_METHOD'] == 'POST'){
		$name = $_POST['name'];
		$email = $_POST['email'];
		$password = password_hash($_POST['password'], PASSWORD_DEFAULT);
		
		require_once 'connect.php';
		
		// Check if email is available
		$sql = "SELECT email from users_table WHERE email = '$email'";
		$respone = mysqli_query($conn, $sql);
		
		if($response->num_rows > 0){
			$result["success"] = "-1";
		}
		else {
			$sql = "INSERT INTO users (name, email, password) VALUES ('$name', '$email', '$password')"; 
			if(mysqli_query($conn, $sql)){
				$result["success"] = "1";
				$result["message"] = "success";
			}
			else{
				$result["success"] = "0";
				$result["message"] = "error";
			}
			
		}
		
		echo json_encode($result);
		mysqli_close($conn);
	}
	
	
?>