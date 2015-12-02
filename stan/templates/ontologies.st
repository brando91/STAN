ontologies(breadcrumb, notifications) ::= <<
	$header()$
	$breadcrumbs(breadcrumb)$
	
	<ul class="nav nav-tabs ">
  		<li role="presentation"><a href="/annotation">Annotation</a></li>
  		<li role="presentation" class="active"><a href="#">Ontologies</a></li>
  		<li role="presentation"><a href="/set-namespace">Namespace</a></li>
	</ul>
	<br>

	$alerts(notifications)$
	
	<div class="row-fluid">
		<h2>Work in progress..</h2>
		<p>
			At the moment you can only use DBPedia to annotate your tables. 
			Soon it will be possible to import and use other knowledge bases.
		</p>
	</div>
	
	$footer()$
>>