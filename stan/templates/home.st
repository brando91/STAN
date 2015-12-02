home(notifications, hasPrevious, content) ::= <<
	<head>
	    <meta charset="utf-8">
	    <meta name="description" content="">
	    <meta name="author" content="">
	    <title>STAN</title>
	    <link rel="shortcut icon" href="assets/img/favicon.ico" type="image/x-icon">
	
	    <link href="/assets/css/bootstrap.min.css" rel="stylesheet">
	    <link href="/assets/css/jquery-ui.min.css" rel="stylesheet">
	    <link href="/assets/css/jasny-bootstrap.min.css" rel="stylesheet">
	    <link href="/assets/css/cover.css" rel="stylesheet">
	    
	    <script src="/assets/js/jquery-1.11.3.min.js"></script>
	    <script src="/assets/js/bootstrap.min.js"></script>
	</head>
	
	 <div class="site-wrapper">

      <div class="site-wrapper-inner">

        <div class="cover-container">

          <div class="masthead clearfix">
            <div class="inner">
              <h1 class="masthead-brand">Semantic Table Annotation</h1>
              <nav>
                <ul class="nav masthead-nav">
                  <li class="active"><a href="#">Home</a></li>
                  $if(hasPrevious)$<li><a href="/annotation">Resume</a></li>$endif$
                  <li><a href="/start">New Project</a></li>
                </ul>
              </nav>
            </div>
          </div>

          <div class="inner cover">
            <h1 class="cover-heading">STAN</h1>
            <p class="lead">
            	STAN is an online tool that lets you annotate your tables with semantics from popular ontologies and knowledge bases. 
            	In this way you will be able to easily integrate your data with the Linked Open Data Cloud. 
            	Indeed STAN will let you export both the mappings and the resulting RDF triples. 
        	</p>
            <p class="lead">
              $if(hasPrevious)$<a href="/annotation" class="btn btn-lg btn-default">Resume $content$</a> or $endif$<a href="/start" class="btn btn-lg btn-default">Start a new annotation</a>
            </p>
          </div>

          <div class="mastfoot">
            <div class="inner">
              <p>Semantic Table Annotation Tool © 2015</p>
            </div>
          </div>

        </div>

      </div>

    </div>
>>