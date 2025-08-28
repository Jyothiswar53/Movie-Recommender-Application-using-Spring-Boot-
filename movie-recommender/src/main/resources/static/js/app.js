const TMDB_IMG = 'https://image.tmdb.org/t/p/w500';

const els = {
    form: document.getElementById('searchForm'),
    input: document.getElementById('searchInput'),
    recWrap: document.getElementById('recommendations'),
    recEmpty: document.getElementById('recEmpty'),
    recCount: document.getElementById('recCount'),
    details: document.getElementById('details'),
    poster: document.getElementById('posterHolder'),
    title: document.getElementById('title'),
    tagline: document.getElementById('tagline'),
    director: document.getElementById('director'),
    genres: document.getElementById('genres'),
    runtime: document.getElementById('runtime'),
    rating: document.getElementById('rating'),
    release: document.getElementById('release'),
    language: document.getElementById('language'),
    overview: document.getElementById('overview'),
    links: document.getElementById('links'),
    selYear: document.getElementById('selYear'),
};

function initializeBackgroundDots() {
    const dotContainer = document.createElement('div');
    dotContainer.id = 'dotContainer';
    document.body.appendChild(dotContainer);

    for (let i = 0; i < 50; i++) {
        const dot = document.createElement('div');
        dot.className = 'dot';

        const startX = Math.random() * 100;
        const startY = Math.random() * 100;
        dot.style.left = `${startX}vw`;
        dot.style.top = `${startY}vh`;

        const duration = 15 + Math.random() * 15;
        dot.style.animationDuration = `${duration}s`;

        const angle = Math.random() * 2 * Math.PI;
        const distanceX = (Math.cos(angle) * (50 + Math.random() * 50)) * (Math.random() < 0.5 ? -1 : 1); // -50 to 50vw
        const distanceY = (Math.sin(angle) * (50 + Math.random() * 50)) * (Math.random() < 0.5 ? -1 : 1); // -50 to 50vh

        dot.style.transform = `translate(${distanceX}vw, ${distanceY}vh)`;

        dotContainer.appendChild(dot);
    }
}

window.addEventListener('load', initializeBackgroundDots);

function setLoading(node, isLoading) {
    if (isLoading) { node.innerHTML = '<span class="loader"></span>'; }
}

function fetchJson(url) {
    return fetch(url).then(r => {
        if (!r.ok) throw new Error('API error ' + r.status);
        return r.json();
    });
}

function renderRecommendations(list) {
    els.recWrap.innerHTML = '';
    els.recCount.style.display = 'inline-block';
    els.recCount.textContent = `${list.length}`;

    if (!list.length) {
        els.recEmpty.textContent = 'No recommendations found for this title.';
        els.recEmpty.style.display = 'block';
        return;
    }
    els.recEmpty.style.display = 'none';

    const frag = document.createDocumentFragment();
    list.forEach(item => {
        const card = document.createElement('article');
        card.className = 'card';
        card.title = item.title;
        card.innerHTML = `
            <img alt="${item.title}" src="${item.posterPath ? TMDB_IMG + item.posterPath : ''}" onerror="this.style.display='none'"/>
            <div class="meta">
                <div>
                    <h3>${item.title}</h3>
                    <div class="badge">${(item.releaseDate || '').slice(0, 4) || '----'}</div>
                </div>
            </div>`;
        card.addEventListener('click', () => showMovieDetails(item.id));
        frag.appendChild(card);
    });
    els.recWrap.appendChild(frag);
}

function fill(node, value) { node.textContent = value || '—'; }

function humanRuntime(mins) {
    if (!mins) return '—';
    const h = Math.floor(mins / 60), m = mins % 60;
    return `${h ? h + 'h ' : ''}${m ? m + 'm' : ''}.trim()`;
}

function link(href, label) {
    const a = document.createElement('a');
    a.href = href; a.textContent = label; a.target = '_blank'; a.rel = 'noopener noreferrer'; a.className = 'link';
    return a;
}

function renderDetails(details) {
    if (details.posterPath) {
        els.poster.innerHTML = `<img alt="Poster" src="${TMDB_IMG + details.posterPath}" style="width:100%; height:auto; border-radius:12px"/>`;
    } else {
        els.poster.textContent = 'No image';
    }

    els.title.textContent = details.title || 'Untitled';
    els.tagline.textContent = details.tagline || '';
    fill(els.director, details.director);
    fill(els.genres, (details.genres || []).join(', '));
    fill(els.runtime, humanRuntime(details.runtime));
    fill(els.rating, details.voteAverage ? `${details.voteAverage.toFixed(1)} / 10` : (details.omdbImdbRating ? `${details.omdbImdbRating} / 10` : '—'));
    fill(els.release, details.releaseDate || '—');
    fill(els.language, (details.originalLanguage || '').toUpperCase());
    els.overview.textContent = details.omdbPlot && details.omdbPlot !== 'N/A' ? details.omdbPlot : (details.overview || '—');
    els.selYear.style.display = details.releaseDate ? 'inline-block' : 'none';
    els.selYear.textContent = (details.releaseDate || '').slice(0, 4);

    els.links.innerHTML = '';
    if (details.homepage) els.links.appendChild(link(details.homepage, 'Official Site'));
    if (details.imdbId) els.links.appendChild(link(`https://www.imdb.com/title/${details.imdbId}/`, 'IMDb'));
    els.links.appendChild(link(`https://www.themoviedb.org/movie/${details.id}`, 'TMDb'));
}

async function showMovieDetails(movieId) {
    els.poster.innerHTML = '<span class="loader"></span>';
    els.title.textContent = 'Loading…';
    els.tagline.textContent = '';
    els.overview.textContent = '';
    try {
        const details = await fetchJson(`/api/details/${movieId}`);
        if (!details) throw new Error('No details found');
        renderDetails(details);
    } catch (e) {
        console.error(e);
        els.title.textContent = 'Failed to load details';
        els.poster.textContent = '—';
    }
}

async function handleSearch(query) {
    els.poster.innerHTML = '<span class="loader"></span>';
    els.title.textContent = 'Loading…';
    els.recEmpty.style.display = 'block';
    els.recEmpty.innerHTML = '<span class="loader"></span> Fetching…';
    els.recWrap.innerHTML = '';
    els.recCount.style.display = 'none';

    try {
        const found = await fetchJson(`/api/search?query=${encodeURIComponent(query)}`);
        if (!found) {
            els.recEmpty.textContent = 'No movie found. Try another title.';
            els.title.textContent = 'No movie selected';
            els.poster.textContent = 'No movie selected';
            return;
        }
        await showMovieDetails(found.id);
        const recs = await fetchJson(`/api/recommendations/${found.id}`);
        renderRecommendations(recs);
    } catch (err) {
        console.error(err);
        els.recEmpty.textContent = 'Something went wrong. Check your internet or try again.';
        els.title.textContent = 'No movie selected';
        els.poster.textContent = 'No movie selected';
    }
}

els.form.addEventListener('submit', (e) => {
    e.preventDefault();
    const q = els.input.value.trim();
    if (!q) return;
    handleSearch(q);
});

let t;
els.input.addEventListener('input', () => {
    clearTimeout(t);
    const q = els.input.value.trim();
    if (q.length < 3) return;
    t = setTimeout(() => handleSearch(q), 40);
});