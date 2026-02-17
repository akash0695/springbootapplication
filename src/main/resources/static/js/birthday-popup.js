/**
 * Birthday Popup for Sakshi (CEO's Wife)
 * Displays on home page load with virtual birthday wishes
 */
(function() {
    'use strict';

    const BIRTHDAY_MODAL_KEY = 'sakshi_birthday_popup_shown';
    const COLORS = ['#ff6b6b', '#feca57', '#48dbfb', '#ff9ff3', '#54a0ff', '#5f27cd', '#00d2d3', '#ff9f43'];

    function createConfetti() {
        const container = document.getElementById('confettiContainer');
        if (!container) return;

        const pieces = 50;
        for (let i = 0; i < pieces; i++) {
            const piece = document.createElement('div');
            piece.className = 'confetti-piece';
            piece.style.left = Math.random() * 100 + '%';
            piece.style.background = COLORS[Math.floor(Math.random() * COLORS.length)];
            piece.style.borderRadius = Math.random() > 0.5 ? '50%' : '0';
            piece.style.animationDelay = Math.random() * 2 + 's';
            piece.style.animationDuration = (3 + Math.random() * 3) + 's';
            container.appendChild(piece);

            setTimeout(() => piece.remove(), 6000);
        }
    }

    function showBirthdayModal() {
        const modalEl = document.getElementById('birthdayModal');
        if (!modalEl) return;

        const modal = new bootstrap.Modal(modalEl, {
            backdrop: 'static',
            keyboard: true
        });

        modal.show();
        createConfetti();

        modalEl.addEventListener('shown.bs.modal', function onShown() {
            createConfetti();
            setTimeout(createConfetti, 500);
            modalEl.removeEventListener('shown.bs.modal', onShown);
        });
    }

    function init() {
        const modalShown = sessionStorage.getItem(BIRTHDAY_MODAL_KEY);
        if (!modalShown) {
            setTimeout(showBirthdayModal, 500);
            sessionStorage.setItem(BIRTHDAY_MODAL_KEY, 'true');
        }
    }

    if (document.readyState === 'loading') {
        document.addEventListener('DOMContentLoaded', init);
    } else {
        init();
    }
})();
