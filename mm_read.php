<?php

$servername = "localhost";
$username = "root";
$password = "";
$dbname = "mm_grocerydb";

// Connect with database demo
$conn = new mysqli($servername, $username, $password, $dbname);

// An array to display response
$response = array();

// Check if the parameter sent is id or not
if ($_POST['mm_id']) {
    // Retrieve the item for the specific id
    $mm_id = $_POST['mm_id'];

    // Select the course detail with the given id
    $stmt = $conn->prepare("SELECT mm_itm_name, mm_itm_price, mm_itm_qty FROM mm_tblitems WHERE mm_id = ?");
    $stmt->bind_param("s", $mm_id);
    $result = $stmt->execute();

    // Check if the table contains data for the specific id
    if ($result == TRUE) {
        // If response is found, return success message
        $response['error'] = false;
        $response['message'] = "Retrieval Successful!";

        // Store and bind result
        $stmt->store_result();
        $stmt->bind_result($mm_itm_name, $mm_itm_price, $mm_itm_qty);
        $stmt->fetch();

        // Pass data to response array
        $response['mm_itm_name'] = $mm_itm_name;
        $response['mm_itm_price'] = $mm_itm_price;
        $response['mm_itm_qty'] = $mm_itm_qty;
    } else {
        // If the entered id does not exist, return error message
        $response['error'] = true;
        $response['message'] = "Incorrect id";
    }
} else {
    // If no parameter is provided in the request, return error message
    $response['error'] = true;
    $response['message'] = "Insufficient Parameters";
}

// Output the response as JSON
echo json_encode($response);

?>
