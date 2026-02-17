$(document).ready(function() {
    $('#contactForm').on('submit', function(e) {
        e.preventDefault();
        
        // Get form data
        var name = $('#name').val();
        var email = $('#email').val();
        var message = $('#message').val();
        
        // Validate form
        if (!name || !email || !message) {
            alert('Please fill in all required fields.');
            return;
        }
        
        // Disable submit button to prevent double submission
        $('#submitBtn').prop('disabled', true).text('Submitting...');
        
        // Create form data
        var formData = new FormData();
        formData.append('name', name);
        formData.append('email', email);
        formData.append('message', message);
        
        // Submit form to backend
        $.ajax({
            url: '/api/queries/submit',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify({
                name: name,
                email: email,
                message: message
            }),
            success: function(response) {
                // Show success message
                alert('Thank you for your interest! We will connect with you shortly.');
                
                // Reset form
                $('#contactForm')[0].reset();
                
                // Re-enable submit button
                $('#submitBtn').prop('disabled', false).text('Submit');
            },
            error: function(xhr, status, error) {
                // Show error message
                alert('Sorry, there was an error submitting your form. Please try again.');
                console.error('Form submission error:', error);
                
                // Re-enable submit button
                $('#submitBtn').prop('disabled', false).text('Submit');
            }
        });
    });
}); 