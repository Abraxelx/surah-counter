$(document).ready(function(){if(localStorage.getItem("lasActiveSurah")){let a=localStorage.getItem("lasActiveSurah");$("#lastActiveSurah").append("Son Okunan: "+a.split("-")[0]+" tane "+a.split("-")[1])}});function showLoadingSpinner(a,e,t,i){e.preventDefault();let l=jQuery(a).closest("form");bootbox.confirm({title:"Teyit",centerVertical:!0,message:t+" adet "+i+" okuduğunuza emin misiniz?",locale:"tr",buttons:{cancel:{label:'<i class="fa fa-times"></i> İptal'},confirm:{label:'<i class="fa fa-check"></i> Onayla'}},callback:function(a){!0===a?($("#updateSurahCountForm1").append('<div class="loading-spinner"><i class="fa-solid fa-spinner fa-spin-pulse fa-lg" style="color: #0d6efd; text-align: center"></i></div>'),localStorage.setItem("lasActiveSurah",t+"-"+i),l.submit()):!1===a&&console.log("This was logged in the callback: "+a)}})}

$(document).ready(function() {

    $('.counter').each(function () {
        $(this).prop('Counter',0).animate({
            Counter: $(this).text()
        }, {
            duration: 2000,
            easing: 'swing',
            step: function (now) {
                $(this).text(Math.ceil(now));
            }
        });
    });

});