error(notifications, breadcrumb, status, content) ::= <<
	$header()$
	$breadcrumbs(breadcrumb)$
	<div class="jumbotron">
		<h1>$status$</h1>
		<br>
		<p>$content$</p>
	</div>
	$footer()$
>>