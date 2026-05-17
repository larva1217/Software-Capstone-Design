/**
 * API 연동 예정 영역
 */
/*document.addEventListener('DOMContentLoaded', fetchMarketIndices);
async function fetchIndices() {
  return null;
}*/

//window.stockViewApi = window.stockViewApi || {};
//window.stockViewApi.fetchIndices = fetchIndices;

//임시 데이터 목록
const SAMPLE_INDICES = [
  { id: "kospi", label: "국내", name: "KOSPI", value: 2650.12, changePct: 0.42, region: "한국" },
  { id: "kosdaq", label: "국내", name: "KOSDAQ", value: 842.33, changePct: -0.18, region: "한국" },
  { id: "nasdaq", label: "미국", name: "NASDAQ", value: 17890.5, changePct: 0.67, region: "미국" },
  { id: "sp500", label: "미국", name: "S&P 500", value: 5980.2, changePct: 0.31, region: "미국" },
  { id: "dow", label: "미국", name: "Dow Jones", value: 43250.0, changePct: -0.12, region: "미국" },
  { id: "nikkei", label: "아시아", name: "Nikkei 225", value: 39200.0, changePct: 0.55, region: "일본" },
  { id: "shanghai", label: "아시아", name: "상해종합", value: 3180.5, changePct: -0.22, region: "중국" },
  { id: "dax", label: "유럽", name: "DAX", value: 19200.0, changePct: 0.19, region: "독일" },
];

//관심 종목을 저장할 때 쓸 이름표
const WATCH_STORAGE_KEY = "stock-view-pins";

function loadPins() {
  try {
    const raw = localStorage.getItem(WATCH_STORAGE_KEY);
    if (!raw) return [];
    const parsed = JSON.parse(raw);
    return Array.isArray(parsed) ? parsed : [];
  } catch {
    return [];
  }
}

function savePins(items) {
  localStorage.setItem(WATCH_STORAGE_KEY, JSON.stringify(items));
}

function formatIndexValue(n) {
  if (n >= 10000) return n.toLocaleString("ko-KR", { maximumFractionDigits: 0 });
  return n.toLocaleString("ko-KR", { minimumFractionDigits: 2, maximumFractionDigits: 2 });
}

function escapeHtml(s) {
  const div = document.createElement("div");
  div.textContent = s;
  return div.innerHTML;
}

function renderWatchList() {
  const list = document.getElementById("watch-list");
  const empty = document.getElementById("watch-empty");
  const pins = loadPins();

  if (!list || !empty) return;

  list.innerHTML = "";
  if (pins.length === 0) {
    empty.classList.add("is-visible");
    return;
  }
  empty.classList.remove("is-visible");

  pins.forEach((symbol, index) => {
    const li = document.createElement("li");
    li.className = "watch-item";
    li.innerHTML = `
      <div class="watch-item__main">
        <span class="watch-item__symbol">${escapeHtml(symbol)}</span>
      </div>
      <button type="button" class="pin-btn" aria-label="핀 해제" aria-pressed="true" data-index="${index}" title="핀 해제">📌</button>
    `;
    list.appendChild(li);
  });

  list.querySelectorAll(".pin-btn").forEach((btn) => {
    btn.addEventListener("click", () => {
      const i = Number(btn.getAttribute("data-index"), 10);
      const next = loadPins().filter((_, j) => j !== i);
      savePins(next);
      renderWatchList();
      showToast("관심 목록에서 제거했습니다.");
    });
  });
}

function addSymbol() {
  const input = document.getElementById("symbol-input");
  if (!input) return;
  const raw = input.value.trim();
  if (!raw) {
    showToast("종목명 또는 티커를 입력하세요.");
    return;
  }
  const pins = loadPins();
  const key = raw;
  if (pins.some((p) => p.toLowerCase() === key.toLowerCase())) {
    showToast("이미 추가된 종목입니다.");
    return;
  }
  pins.push(raw);
  savePins(pins);
  input.value = "";
  renderWatchList();
  showToast("관심 종목에 추가했습니다.");
}

document.addEventListener("DOMContentLoaded", () => {
  initCommonPage();
  //renderIndices();
  renderWatchList();

  document.getElementById("btn-add-symbol")?.addEventListener("click", addSymbol);
  document.getElementById("symbol-input")?.addEventListener("keydown", (e) => {
    if (e.key === "Enter") addSymbol();
  });
});

document.addEventListener('DOMContentLoaded', function() {
    const btnAiAnalyze = document.getElementById('btn-ai-analyze');
    const aiBriefingResult = document.getElementById('ai-briefing-result');

    if(btnAiAnalyze) {
        btnAiAnalyze.addEventListener('click', function() {
            btnAiAnalyze.disabled = true;
            btnAiAnalyze.innerText = "분석 중... ⏳";
            aiBriefingResult.innerHTML = "시장에 흩어진 지표들을 모아 AI가 분석 중입니다. 잠시만 기다려주세요...";

            const marketDataList = [];
            const cards = document.querySelectorAll('.index-card');

            cards.forEach(card => {
                const name = card.querySelector('.index-card__name')?.innerText.trim() || "";
                const price = card.querySelector('.index-card__value')?.innerText.trim() || "";
                const changeRate = card.querySelector('.index-card__change')?.innerText.trim() || "";

                if(name) {
                    marketDataList.push({ name: name, price: price, changeRate: changeRate });
                }
            });

            fetch('/api/ai/analyze-market', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(marketDataList)
            })
            .then(response => {
                if(!response.ok) throw new Error("서버 연동 오류 발생");
                return response.json();
            })
            .then(data => {
                aiBriefingResult.innerHTML = data.analysisResult;
            })
            .catch(error => {
                console.error('AI 분석 실패:', error);
                aiBriefingResult.innerHTML = "<span style='color: #ff4d4d;'>서버와 통신하는 중 오류가 발생했습니다.</span>";
            })
            .finally(() => {
                btnAiAnalyze.disabled = false;
                btnAiAnalyze.innerText = "시장 분석하기";
            });
        });
    }
});
