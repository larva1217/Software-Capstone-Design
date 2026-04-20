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

function bindHeaderActions() {
  document.getElementById("btn-theme")?.addEventListener("click", toggleTheme);
  document.getElementById("btn-alarm")?.addEventListener("click", () => {
    showToast("알람 설정은 추후 연동 예정입니다.");
  });
  document.getElementById("btn-login")?.addEventListener("click", () => {
    showToast("로그인은 추후 연동 예정입니다.");
  });
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
