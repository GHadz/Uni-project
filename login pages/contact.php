<?php

// Replace these values with your own Gmail account information
$smtpHost = 'localhost';
$smtpUsername = 'elio khawand';
$smtpPassword = 'yow';
$smtpPort = 587;

$name = $_POST["name"];
$email = $_POST["email"];
$subject = $_POST["subject"];
$message = $_POST["message"];
// Build email message
$to = "elio.khawand@gmail.com"; // replace with your own email address
$subject = "New message from " . $name . " (" . $email . ")";
$body = "Name: " . $name . "\n\nEmail: " . $email . "\n\nMessage: " . $message;
$headers = "From: " . $name . " <" . $email . ">";

// Set up SMTP connection
$smtp = @\stream_socket_client("$smtpHost:$smtpPort", $errno, $errstr, 15);
if (!$smtp) {
    // SMTP connection failed, display error message to user
    echo '<div class="alert alert-danger">Sorry, Our system is down due to economic crisis in Lebanon. Please for further information, call us on the number above.</div>';
} else {
    // SMTP connection successful, send email
    \fputs($smtp, "EHLO $smtpHost\r\n");
    \fputs($smtp, "STARTTLS\r\n");
    \stream_socket_enable_crypto($smtp, true, STREAM_CRYPTO_METHOD_TLS_CLIENT);
    \fputs($smtp, "AUTH LOGIN\r\n");
    \fputs($smtp, base64_encode("$smtpUsername")."\r\n");
    \fputs($smtp, base64_encode("$smtpPassword")."\r\n");
    \fputs($smtp, "MAIL FROM: <$smtpUsername>\r\n");
    \fputs($smtp, "RCPT TO: <$to>\r\n");
    \fputs($smtp, "DATA\r\n");
    \fputs($smtp, "To: <$to>\r\n");
    \fputs($smtp, "Subject: $subject\r\n");
    \fputs($smtp, "$headers\r\n");
    \fputs($smtp, "\r\n");
    \fputs($smtp, "$body\r\n");
    \fputs($smtp, ".\r\n");
    \fputs($smtp, "QUIT\r\n");
    // Email sent successfully, display success message to user
    echo '<div class="alert alert-success">Your message has been sent. Thank you!</div>';
}

?>
