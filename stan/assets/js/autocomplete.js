$(function() {
 
    $("#annotation").autocomplete({
    	source: function(request, response) {
    		$.ajax({
    			url : "http://abstat.disco.unimib.it/api/v1/autocomplete/properties",
    			data: {
    	            q : request.term,
    	            dataset : "dbpedia-3.9-infobox"
    	        },
    			dataType: "json",
    			success : function(data) {
    				 response($.map(data.response.docs, function(item) {
	    				 return {
	    					 label : item.URI,
	    					 value : item.URI,
	    					 type  : item.type
	    				 }
    				 }));
				 },
    		});
    	},
    	create: function () {
            $(this).data('ui-autocomplete')._renderItem = function (ul, item) {
                return $('<li>')
                    .append(propertyIcon(item.type) + ' ' + item.value)
                    .appendTo(ul);
            };
        },
    	select: function(el, ui) {
    		setPropertyIcon(ui.item.type);
	    }
    });	    
    
    $("#object-type").autocomplete({
    	source: function(request, response) {
    		$.ajax({
    			url : "http://abstat.disco.unimib.it/api/v1/autocomplete/concepts",
    			data: {
    	            q : request.term,
    	            dataset : "dbpedia-3.9-infobox"
    	        },
    			dataType: "json",
    			success : function(data) {
    				 response($.map(data.response.docs, function(item) {
	    				 return {
	    					 label : item.URI,
	    					 value : item.URI,
	    					 type  : item.type
	    				 }
    				 }));
				 },
    		});
    	},
    	select: function(el, ui) {
    		$('#typeSet').val(ui.item.type);
	    }
    });

    $("#type").autocomplete({
    	source: function(request, response) {
    		$.ajax({
    			url : "http://abstat.disco.unimib.it/api/v1/autocomplete/concepts",
    			data: {
    	            q : request.term,
    	            dataset : "dbpedia-3.9-infobox"
    	        },
    			dataType: "json",
    			success : function(data) {
    				 response($.map(data.response.docs, function(item) {
	    				 return {
	    					 label : item.URI,
	    					 value : item.URI
	    				 }
    				 }));
				 },
    		});
    	}
    });	
});


function propertyIcon(type){
	if(type == 'objectProperty'){
		return '<span class="label label-info small" aria-hidden="true">O</span>';
	}
	else{
		return '<span class="label label-success small" aria-hidden="true">D</span>';
	}
}

function setPropertyIcon(type){
	if(type == 'objectProperty'){
		$('#property-icon-type').text('O');
		$('#property-icon-type').removeClass('btn-default btn-success').addClass('btn-info');
	}
	else if(type == 'datatypeProperty'){
		$('#property-icon-type').text('D');
		$('#property-icon-type').removeClass('btn-default btn-info').addClass('btn-success');
	}
	else{
		$('#property-icon-type').text('-');
		$('#property-icon-type').removeClass('btn-info btn-success').addClass('btn-default');
	}
}