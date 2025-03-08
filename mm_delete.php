<?php

$servername = "localhost";

// For testing, the username is root.
$username = "root";

// The password for testing is blank.
$password = "";

// Database name
$dbname = "mm_grocerydb";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// An array to display response
$response = array();

if ($_POST['mm_id']) {
    // Retrieve the item for the specific id
    $mm_id = $_POST['mm_id'];

    // Delete the item with the given id
    $stmt = $conn->prepare("DELETE FROM mm_tblitems WHERE mm_id = ?");
    $stmt->bind_param("s", $mm_id);
    $result = $stmt->execute();

    // Check if the deletion was successful
    if ($result == TRUE) {
        $response['error'] = false;
        $response['message'] = "Deletion Successful!";
    } else {
        $response['error'] = true;
        $response['message'] = "Incorrect id";
    }
} else {
    $response['error'] = true;
    $response['message'] = "Insufficient Parameters";
}

// Output the response as JSON
echo json_encode($response);

?>
