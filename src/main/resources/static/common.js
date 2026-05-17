function showToast(message) {
  const el = document.getElementById("toast");
  if (!el) return;
  el.textContent = message;
  el.classList.add("is-visible");
  clearTimeout(showToast._t);
  showToast._t = setTimeout(() => el.classList.remove("is-visible"), 2200);
}

function initTheme() {
  const stored = localStorage.getItem("stock-view-theme");
  const prefersDark = window.matchMedia("(prefers-color-scheme: dark)").matches;
  const theme = stored === "dark" || stored === "light" ? stored : prefersDark ? "dark" : "light";
  document.documentElement.setAttribute("data-theme", theme);
}

function toggleTheme() {
  const isDark = document.documentElement.getAttribute("data-theme") === "dark";
  const next = isDark ? "light" : "dark";
  document.documentElement.setAttribute("data-theme", next);
  localStorage.setItem("stock-view-theme", next);
}

function bindUserMenu() {
  const btn = document.getElementById("btn-user");
  const popup = document.getElementById("user-menu-popup");
  if (!btn || !popup) return;

  btn.addEventListener("click", (e) => {
    e.stopPropagation();
    const open = popup.hidden;
    popup.hidden = !open;
    btn.setAttribute("aria-expanded", String(open));
  });

  document.addEventListener("click", (e) => {
    if (popup.hidden) return;
    if (!popup.contains(e.target) && !btn.contains(e.target)) {
      popup.hidden = true;
      btn.setAttribute("aria-expanded", "false");
    }
  });
}

function bindHeaderActions() {
  document.getElementById("btn-theme")?.addEventListener("click", toggleTheme);
  bindUserMenu();
}

function initCommonPage() {
  initTheme();
  bindHeaderActions();
}

if (document.readyState === "loading") {
  document.addEventListener("DOMContentLoaded", initCommonPage);
} else {
  initCommonPage();
}
