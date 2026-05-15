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
      credentials: "same-origin"
    });
    const raw = await res.text();
    let message = raw;
    try {
      const json = JSON.parse(raw);
      if (json && typeof json.message === "string") {
        message = json.message;
      }
    } catch (_) {}
    return { ok: res.ok, text: message };
  }

  function readForm(form) {
    const username = (form.querySelector("#username")?.value || "").trim();
    const password = form.querySelector("#password")?.value || "";
    return { username, password };
  }

  function bindLogin() {
    const form = document.getElementById("form-login");
    const msg = document.getElementById("auth-message");
    if (!form || typeof API_LOGIN === "undefined") return;

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const { username, password } = readForm(form);
      if (!username || !password) {
        setMessage(msg, "아이디와 비밀번호를 입력해 주세요.", true);
        return;
      }
      const submitBtn = form.querySelector('button[type="submit"]');
      if (submitBtn.disabled) return;
      submitBtn.disabled = true;
      submitBtn.textContent = "로그인 중...";
      setMessage(msg, "", false);
      try {
        const { ok, text } = await postJson(API_LOGIN, { username, password });
        if (!ok) {
          setMessage(msg, text || "로그인에 실패했습니다.", true);
          return;
        }
        setMessage(msg, text || "로그인 성공!", false);
        setTimeout(() => { location.href = HOME_URL; }, 600);
      } catch (err) {
        console.error(err);
        setMessage(msg, "네트워크 오류가 발생했습니다.", true);
      } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = "로그인";
      }
    });
  }

  function bindSignup() {
    const form = document.getElementById("form-signup");
    const msg = document.getElementById("auth-message");
    if (!form || typeof API_SIGNUP === "undefined") return;

    form.addEventListener("submit", async (e) => {
      e.preventDefault();
      const { username, password } = readForm(form);
      if (!username || !password) {
        setMessage(msg, "아이디와 비밀번호를 입력해 주세요.", true);
        return;
      }
      const submitBtn = form.querySelector('button[type="submit"]');
      if (submitBtn.disabled) return;
      submitBtn.disabled = true;
      submitBtn.textContent = "가입 중...";
      setMessage(msg, "", false);
      try {
        const { ok, text } = await postJson(API_SIGNUP, { username, password });
        if (!ok) {
          setMessage(msg, text || "회원가입에 실패했습니다.", true);
          return;
        }
        setMessage(msg, text || "회원가입 성공!", false);
        setTimeout(() => { location.href = LOGIN_PAGE; }, 800);
      } catch (err) {
        console.error(err);
        setMessage(msg, "네트워크 오류가 발생했습니다.", true);
      } finally {
        submitBtn.disabled = false;
        submitBtn.textContent = "가입하기";
      }
    });
  }

  bindLogin();
  bindSignup();
})();
