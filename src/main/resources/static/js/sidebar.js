document.addEventListener("DOMContentLoaded", function () {
    const sidebar = document.getElementById("adminSidebar");
    const toggleBtn = document.getElementById("toggleSidebarBtn");

    if (!sidebar || !toggleBtn) return;

    let hideTimeout;

    toggleBtn.addEventListener("mouseenter", () => {
        sidebar.classList.remove("translate-x-full");
        sidebar.classList.add("translate-x-0");
        clearTimeout(hideTimeout);
    });

    toggleBtn.addEventListener("mouseleave", () => {
        hideTimeout = setTimeout(() => {
            sidebar.classList.remove("translate-x-0");
            sidebar.classList.add("translate-x-full");
        }, 500);
    });

    sidebar.addEventListener("mouseenter", () => {
        clearTimeout(hideTimeout);
    });

    sidebar.addEventListener("mouseleave", () => {
        hideTimeout = setTimeout(() => {
            sidebar.classList.remove("translate-x-0");
            sidebar.classList.add("translate-x-full");
        }, 500);
    });
});
