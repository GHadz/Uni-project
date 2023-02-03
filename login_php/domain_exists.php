<?php
function domain_exists($email, $record = 'MX'){
    list($user, $domain) = explode('@', $email);
    if($domain == 'hotmail.com' ||$domain == 'gmail.com'||$domain == 'st.ul.edu.lb'||$domain == 'outlook.com'||$domain == 'outlook.sa'){return true;}
	return false;
}
