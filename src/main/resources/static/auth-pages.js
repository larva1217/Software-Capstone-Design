(function () {
  function setMessage(el, text, isError) {
    if (!el) return;
    el.textContent = text;
    el.hidden = !text;
    el.classList.toggle("is-error", Boolean(isError && text));
    el.classList.toggle("is-success", Boolean(!isError && text));
  }

  async function postJson(url, body) {
    const res = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(body),
    });
    const text = await res.text();
    return { ok: res.ok, text };
  }

  function bindLogin() {
    const form = document.getElementById("form-login");
    const msg = document.getElementById("auth-message");
    if (!form || typeof API_LOGIN === "undefined") return;

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const username = (form.querySelector("#username") || {}).value?.trim() || "";
      const password = (form.querySelector("#password") || {}).value || "";
      if (!username || !password) {
        setMessage(msg, "아이디와 비밀번호를 입력해 주세요.", true);
        return;
      }
      setMessage(msg, "", false);
      try {
        const { ok, text } = await postJson(API_LOGIN, { username, password });
        if (ok) {
          setMessage(msg, text, false);
          const next =
            form.getAttribute("data-redirect") ||
            (typeof HOME_URL !== "undefined" ? HOME_URL : "/");
          window.location.href = next;
        } else {
          setMessage(msg, text, true);
        }
      } catch {
        setMessage(msg, "네트워크 오류가 발생했습니다.", true);
      }
    });
  }

  function bindSignup() {
    const form = document.getElementById("form-signup");
    const msg = document.getElementById("auth-message");
    if (!form || typeof API_SIGNUP === "undefined") return;

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const username = (form.querySelector("#username") || {}).value?.trim() || "";
      const password = (form.querySelector("#password") || {}).value || "";
      if (!username || !password) {
        setMessage(msg, "아이디와 비밀번호를 입력해 주세요.", true);
        return;
      }
      setMessage(msg, "", false);
      try {
        const { ok, text } = await postJson(API_SIGNUP, { username, password });
        if (ok) {
          setMessage(msg, text, false);
          setTimeout(() => {
            window.location.href =
              typeof LOGIN_PAGE !== "undefined" ? LOGIN_PAGE : "/login";
          }, 900);
        } else {
          setMessage(msg, text, true);
        }
      } catch {
        setMessage(msg, "네트워크 오류가 발생했습니다.", true);
      }
    });
  }

  function run() {
    bindLogin();
    bindSignup();
  }

  if (document.readyState === "loading") {
    document.addEventListener("DOMContentLoaded", run);
  } else {
    run();
  }
})();
