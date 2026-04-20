async function fetchNewsFeed() {
  return null;
}

window.stockViewApi = window.stockViewApi || {};
window.stockViewApi.fetchNewsFeed = fetchNewsFeed;

const SAMPLE_NEWS = [
  {
    id: "n1",
    source: "데모 경제",
    time: "오늘 09:12",
    title: "국내 증시, 외국인 매수에 상승 마감",
    summary: "코스피는 전 거래일 대비 소폭 상승하며 마감했습니다. (샘플 기사)",
  },
  {
    id: "n2",
    source: "데모 마켓",
    time: "어제 18:40",
    title: "글로벌 금리 동향이 증시 변동성에 미치는 영향",
    summary: "주요국 중앙은행의 정책 기조가 투자 심리를 좌우하고 있습니다. (샘플)",
  },
  {
    id: "n3",
    source: "데모 테크",
    time: "어제 14:05",
    title: "반도체 업종, 실적 시즌 앞두고 관심 집중",
    summary: "업계별 전망과 공급망 이슈가 화두입니다. (샘플)",
  },
];

function escapeHtml(s) {
  const div = document.createElement("div");
  div.textContent = s;
  return div.innerHTML;
}

function renderNewsFeed() {
  const root = document.getElementById("news-list");
  if (!root) return;

  root.innerHTML = SAMPLE_NEWS.map(
    (item) => `
    <article class="news-card" data-news-id="${item.id}">
      <div class="news-card__meta">${escapeHtml(item.source)} · ${escapeHtml(item.time)}</div>
      <h2 class="news-card__title">${escapeHtml(item.title)}</h2>
      <p class="news-card__summary">${escapeHtml(item.summary)}</p>
    </article>
  `
  ).join("");
}

document.addEventListener("DOMContentLoaded", () => {
  initCommonPage();
  renderNewsFeed();
});
