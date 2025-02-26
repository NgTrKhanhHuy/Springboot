$(document).ready(function(){
    $(".add-to-cart").click(function(){
        const productId = $(this).data("product-id");
        $.ajax({
            url: '/cart/add/' + productId,
            type: 'POST',
            success: function(cartCount) {
                // Cập nhật số lượng sản phẩm trên header
                $("#cartItemCount").text(cartCount);
                // Hiển thị thông báo thành công (nếu muốn)
                alert("Đã thêm sản phẩm vào giỏ hàng!"+ cartCount);
            },
            error: function(jqXHR) {
                alert("Có lỗi xảy ra," + jqXHR.responseText);
            }
        });
    });
});
$.ajaxSetup({
    headers: {
        'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
    }
});

$.ajaxSetup({
    headers: {
        'X-CSRF-TOKEN': $('meta[name="_csrf"]').attr('content')
    }
});

