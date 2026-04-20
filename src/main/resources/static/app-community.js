async function fetchCommunityPosts() {
  return null;
}

window.stockViewApi = window.stockViewApi || {};
window.stockViewApi.fetchCommunityPosts = fetchCommunityPosts;

const SAMPLE_POSTS = [
  { id: 1, author: "asd", content: "asdasd", likes: 12 },
  { id: 2, author: "qwe", content: "qweqwe", likes: 5 },
];

function escapeHtml(s) {
  const div = document.createElement("div");
  div.textContent = s;
  return div.innerHTML;
}

function renderPostList(container) {
  const list = container.querySelector(".post-list");
  if (!list) return;

  list.innerHTML = SAMPLE_POSTS.map(
    (post) => `
    <div class="post-card" data-post-id="${post.id}">
      <div class="post-author">${escapeHtml(post.author)}</div>
      <div class="post-content">${escapeHtml(post.content)}</div>
      <div class="post-footer">👍 ${post.likes}</div>
    </div>
  `
  ).join("");
}

function initCommunity() {
  const container = document.getElementById("view-community");
  if (!container) return;

  container.innerHTML = `
    <div class="page-head">
      <h1>커뮤니티</h1>
      <p class="muted">실시간 토론에 참여해보세요.</p>
    </div>
    <div class="community-write">
      <textarea id="post-input" class="input" placeholder="무슨 생각을 하고 계신가요?"></textarea>
      <button type="button" class="btn btn-primary" id="btn-save-post">글쓰기</button>
    </div>
    <div class="post-list"></div>
  `;

  renderPostList(container);

  document.getElementById("btn-save-post")?.addEventListener("click", () => {
    showToast("백엔드 API가 준비되면 실제로 저장됩니다.");
  });
}

document.addEventListener("DOMContentLoaded", () => {
  initCommonPage();
  initCommunity();
});
