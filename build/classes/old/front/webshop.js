// Closure koji će zapamtiti trenutni proizvod


function clickClosure(product){
	return function() {
		// Parametar product prosleđen u gornju funkciju će biti vidljiv u ovoj
		// Ovo znači da je funkcija "zapamtila" za koji je proizvod vezana
		$('tr.selected').removeClass('selected');
		$(this).addClass('selected');
		
		// let name = $('input[name="name"]').val();
		
		$('input[name=productUpdatePrice]').val(this.lastChild.textContent);
		$('input[name=productUpdateName]').val(this.firstChild.textContent);
		
		$('form#productUpdate').show();
		
	};
}


function addProductTr(product) {
	let tr = $('<tr></tr>');
	let tdNaziv = $('<td>' + product.name + '</td>');
	let tdCena = $('<td>' + product.price + '</td>');
	tr.append(tdNaziv).append(tdCena);
	tr.click(clickClosure(product));
	$('#tabela tbody').append(tr);
}

$(document).ready(function() {
	// Ono sto se poziva pri prvobitnom ucitavanju stranice, efektivno ON_LOAD
	$.get({ 		
		url: 'rest/products',
		success: function(products) {
			for (let product of products) {
				addProductTr(product);
			}
		}
	});
	
	$('button#dodaj').click(function() {
		$('form#forma').show();
	});
	
	$('button#skloni').click(function() {
		$('form#forma').hide();
		$('form#productUpdate').hide();
	});
	
	$('form#forma').submit(function(event) {
		event.preventDefault();
		let name = $('input[name="name"]').val();
		let price = $('input[name="price"]').val();
		if (!name) {
			$('#error').text('Proizvod mora imati ime!');
			$("#error").show().delay(3000).fadeOut();
			return;
		}
		$('p#eropr').hide();
		if (!price || isNaN(price)) {
			$('#error').text('Cena mora biti broj!');
			$("#error").show().delay(3000).fadeOut();
			return;
		}
		$('p#error').hide();
		$.post({
			url: 'rest/products',
			data: JSON.stringify({id: '', name: name, price: price}),
			contentType: 'application/json',
			success: function(product) {
				$('#success').text('Novi proizvod uspešno kreiran.');
				$("#success").show().delay(3000).fadeOut();
				// Dodaj novi proizvod u tabelu
				addProductTr(product);
			}
		});
	});
	
	$('form#productUpdate').submit(function(event) {
		event.preventDefault();
		let name = $('input[name="productUpdateName"]').val();
		let price = $('input[name="productUpdatePrice"]').val();
		if (!name) {
			$('#error').text('Proizvod mora imati ime!');
			$("#error").show().delay(3000).fadeOut();
			return;
		}
		$('p#eropr').hide();
		if (!price || isNaN(price)) {
			$('#error').text('Cena mora biti broj!');
			$("#error").show().delay(3000).fadeOut();
			return;
		}
		$('p#error').hide();
		$.put({
			url: 'rest/products',
			data: JSON.stringify({id: '', name: name, price: price}),
			contentType: 'application/json',
			success: function(product) {
				$('#success').text('Novi proizvod uspešno kreiran.');
				$("#success").show().delay(3000).fadeOut();
				// Dodaj novi proizvod u tabelu
				addProductTr(product);
			}
		});
	});
	
});