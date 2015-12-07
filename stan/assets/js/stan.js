/****************************************** Open Modal **********************************************/

function prepareModal(id){
	var uri = $('#' + id).attr('title');
	$('#annotation').val(uri);
	$('#object-type').val($('#' + id).attr('object'));
	$('#typeSet').val($('#' + id).attr('property-type'));
	setPropertyIcon('');
	$('#subject-type').val($('#' + currentSubject()).attr('title'));
	$('#column-id').val(id);
	$('#type').popover('hide');
	$('#annotation').popover('hide');
	
	if($('#' + id).hasClass('subject')) {
		$('#isSubject').prop('checked', true);
		$('#type').prop('disabled', false);
		$('#type').val(uri);
		$('#annotation').val("");
	}
	else {
		$('#isSubject').prop('checked', false);
		$('#type').val('');
		$('#type').prop('disabled', true);
	}

	showSuggestions(id);
}

/****************************************** Annotate **********************************************/

function annotate(){
	if(isCompiled('#annotation')){
		updateHeader($('#column-id').val(), $('#annotation').val(), $('#object-type').val(), $('#typeSet').val());
		$('#annotationPopup').modal('hide');
	}
}

function setSubject(){
	var column = $('#column-id').val();
	var previous = currentSubject();
	
	if($('#isSubject').is(':checked')){
		if(isCompiled('#type')){
			updateHeader(column, $('#type').val(), '', '');
			$('#' + previous).removeClass('subject');
			$('#' + column).addClass('subject');
			$('#annotationPopup').modal('hide');
		}
	}
	else{
		$('#' + column).removeClass('subject');	
		$('#annotationPopup').modal('hide');
	}
}

function isCompiled(input){
	if($(input).val() != ""){
		$(input).popover('hide');
		return true;
	}
	else{
		$(input).popover('show');
		return false;
	}
}

$(function() {
	$('#isSubject').change(function(){
		if($(this).is(':checked')){
			$('#type').prop('disabled', false);
		} else {
			$('#type').prop('disabled', true);
		}
	});
});

function updateHeader(column, annotation, object, typeSet){
	var label = getLabelFor(annotation);
	
	$('#' + column).text(label);
	$('#' + column).attr('onclick',"prepareModal('" + column + "')");
	$('#' + column).attr('title', annotation);
	$('#' + column).attr('object', object);
	$('#' + column).attr('property-type', typeSet);
	$('#' + column).removeClass('subject concept datatype');
	(typeSet == '') ? $('#' + column).addClass('datatype') : $('#' + column).addClass(typeSet);
	
}

/****************************************** Suggestions **********************************************/

function showSuggestions(id){
	$("#loading").show();
	$("#candidates").hide();
	$("#candidates").empty();

	var columnId = id.split("-")[1];
	$.post("/suggestions", {kb: $("#knowledgeBase").val(), column: columnId, context: currentContext(columnId)}, function() {})
		.done(function(data, status){
			var candidates = data.annotations;	
			if(candidates.length == 0) noSuggestionsFound();
			for(var i = 0; i < candidates.length; i++) {
		    	var candidate = candidates[i];
				var element = document.createElement("a");
				element.setAttribute("class", "list-group-item");
				element.setAttribute("onclick", "takeSuggestion('" + candidate.uri + "')");
				element.innerHTML = candidate.uri + " - " + candidate.label;
				$("#candidates").append(element);
			}
			$("#loading").hide();
			$("#candidates").show();
		})
		.fail(function() {
			$("#loading").hide();
			noSuggestionsFound();
			$("#candidates").show();
		});
}

function noSuggestionsFound(){
	var element = document.createElement("li");
	element.setAttribute("class", "list-group-item");
	element.innerHTML = "Sorry, no suggestions found.";
	$("#candidates").append(element);
}

function takeSuggestion(annotation){
	$('#annotation').val(annotation);
	setPropertyIcon('');
}

function currentContext(column){
	var context = $('#' + currentSubject()).text();
	return (context == '') ? $('#table-name').text() : context;
}

/****************************************** Utils **********************************************/

function loading() {
	if($('#table').val() != '' && $('#separator').val() != ''){
		$('#upload').text("Uploading...");
	}
}

function loadingUrl() {
	if($('#url').val() != '' && $('#separator-url').val() != ''){
		$('#upload-url').text("Uploading...");
	}	
}

function getLabelFor(annotation){
	var tokens = annotation.split("/");
	return tokens[tokens.length-1];
}

function currentSubject(){
	return $(".subject").attr("id");
}

function currentAnnotations(){
	var totColumns = $("table").find("tr:nth-child(1) td").length;
	var annotations = {
		    columns : []
		};
	for(var i = 1; i <= totColumns; i++){
		var annotation = $("#header-" + i);
		annotations.columns.push({
			"column" : i,
			"uri" : annotation.attr("title"),
			"label" : annotation.text(),
			"object-type" : annotation.attr("object"),
			"property-type" : annotation.attr("property-type")
		});
	}
	return JSON.stringify(annotations);
}

function showMessage(text, type){
	var div = document.createElement("div");
	div.setAttribute('class', "alert alert-dismissible alert-" + type);
	div.setAttribute('role', "alert");
	var button = document.createElement("button");
	button.setAttribute('class', "close");
	button.setAttribute('data-dismiss', "alert");
	button.setAttribute('aria-label', "Close");
	var span = document.createElement("span");
	span.setAttribute('aria-hidden', "true");
	span.innerHTML = '&times;';

	$(button).append(span);
	$(div).append(button);
	$(div).append(text);	
	$("#alert-message").append(div);
} 

/****************************************** Save **********************************************/

$(function() {
	$("#save").click(function() {
		$("#save").text("Saving...");
		
		$.post("/save-status", {annotations: currentAnnotations(), subject: currentSubject()}, function() {})
		  .done(function() {
			  showMessage("Saved current status", "success");
			  $("#save").text("Save");
		  })
		  .fail(function() {
			  	showMessage("Error saving the current status", "danger");
		    	$("#save").text("Save");
		  });
	});
});

/****************************************** Export **********************************************/

$(function() {
	$("#export-mappings").click(function() {
		$("#export-label").text("Exporting...");
		
		$.post("/export/mappings", {annotations: currentAnnotations(), subject: currentSubject()}, function() {})
		  .done(function(xhr, status, error) {
			  showMessage("Export completed", "success");
			  $("#export-label").text("Export");
			  window.open("/downloads/mappings", "_self");
		  })
		  .fail(function(xhr, status, error) {
		  	showMessage(xhr.responseText, "danger");
		    	$("#export-label").text("Export");
		  });
	});
});

$(function() {
	$("#export-triples").click(function() {
		$("#export-label").text("Exporting...");
		
		$.post("/export/triples", {annotations: currentAnnotations(), subject: currentSubject()}, function() {})
		  .done(function(xhr, status, error) {
			  showMessage("Export completed", "success");
			  $("#export-label").text("Export");
			  window.open("/downloads/triples", "_self");
		  })
		  .fail(function(xhr, status, error) {
		  	showMessage(xhr.responseText, "danger");
		    	$("#export-label").text("Export");
		  });
	});
});

/****************************************** Start Tabs **********************************************/

$(function() {
	$('#fromFile').click(function (e) {
		e.preventDefault()
		$(this).tab('show');
	});
	
	$('#fromUrl').click(function (e) {
		e.preventDefault()
		$(this).tab('show');
	});
});
