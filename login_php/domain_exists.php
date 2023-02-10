<?php
function domain_exists($email, $record = 'MX'){
    list($user, $domain) = explode('@', $email);
    if($domain == 'st.ul.edu.lb'){return true;}
	return false;
}
