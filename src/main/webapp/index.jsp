<!DOCTYPE html>
<html lang="en">

<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>URL Shortener</title>

  <style>
    * {
      box-sizing: border-box;
      margin: 0;
      padding: 0;
    }

    body {
      font-family: Arial, sans-serif;
    }

    h1 {
      color: royalblue;
      text-align: center;
      padding: 15px 0;
      text-transform: uppercase;
    }

    form {
      display: flex;
      flex-direction: column;
      align-items: center;
      margin-top: 30px;
    }

    input {
      padding: 8px;
      width: 300px;
    }

    button {
      background-color: royalblue;
      color: white;
      border: none;
      padding: 6px 10px;
      margin: 10px;
      cursor: pointer;
      border-radius: 4px;
    }

    #shortURL {
      display: none;
      justify-content: center;
      gap: 15px;
      margin-top: 20px;
      align-items: center;
    }

    table {
      margin: auto;
      border-collapse: collapse;
      width: 80%;
      margin-top: 20px;
    }

    th,
    td {
      border: 1px solid #ddd;
      padding: 8px;
      text-align: center;
    }

    th {
      background-color: royalblue;
      color: white;
    }

    #analyticsSection {
      display: none;
      margin-top: 20px;
    }
  </style>
</head>

<body>

  <h1>URL Shortener</h1>
  <form onsubmit="shortenURL(event)">
    <div>
      <label>Enter URL: </label><br>
      <input id="longURL" type="text" placeholder="https://example.com">
    </div>

    <button type="submit">Shorten URL</button>
  </form>

  <div id="shortURL">
    <a href=""></a>
    <button onclick="copyURL()">Copy</button>
  </div>

  <hr style="margin:40px 0;">

  <h1>Analytics</h1>
  <div style="text-align:center;">
    <input id="analyticsCode" type="text" placeholder="Enter short code">
    <button onclick="loadAnalytics(1)">Load Analytics</button>
  </div>

  <div id="analyticsSection">
    <table>
      <thead>
        <tr>
          <th>IP</th>
          <th>Country</th>
          <th>Device</th>
          <th>Time</th>
        </tr>
      </thead>
      <tbody id="analyticsBody"></tbody>
    </table>

    <div style="text-align:center; margin-top:10px;">
      <button onclick="prevPage()">Prev</button>
      <span id="pageNum">1</span>
      <button onclick="nextPage()">Next</button>
    </div>
  </div>

</body>

<script>
  const urlCont = document.querySelector("#shortURL a");
  const longURL = document.getElementById("longURL");

  let currentPage = 1;

  function copyURL() {
    navigator.clipboard.writeText(urlCont.href);
    alert("Copied!");
  }

  async function shortenURL(event) {
    event.preventDefault();

    const url = longURL.value;

    try {
      const response = await fetch("http://localhost:8080/shorten", {
        method: "POST",
        headers: {
          "Content-Type": "application/x-www-form-urlencoded"
        },
        body: "url=" + encodeURIComponent(url)
      });

      const result = await response.json();

      if (result.url) {
        const shortUrl = window.origin + "/u/" + result.url;

        document.getElementById("shortURL").style.display = "flex";
        urlCont.href = shortUrl;
        urlCont.textContent = shortUrl;

        document.getElementById("analyticsCode").value = result.url;
        loadAnalytics(1);

      } else {
        alert("Error: " + result.error);
      }

    } catch (err) {
      alert("Error: " + err.message);
    }
  }

  async function loadAnalytics(page) {
    const code = document.getElementById("analyticsCode").value.trim();

    if (!code) {
      alert("Enter short code");
      return;
    }

    try {
      const response = await fetch(`http://localhost:8080/analytics/${code}?page=${page}`);

      if (!response.ok) {
        const text = await response.text();
        throw new Error(text);
      }

      const result = await response.json();

      const tbody = document.getElementById("analyticsBody");
      tbody.innerHTML = "";

      result.data.forEach(click => {
        tbody.innerHTML += `
          <tr>
            <td>${click.ip}</td>
            <td>${click.country || "Unknown"}</td>
            <td>${click.device || "Unknown"}</td>
            <td>${click.time}</td>
          </tr>
        `;
      });

      if (result.data.length === 0) {
        alert("No analytics found for this short code");
        return;
      }

      document.getElementById("analyticsSection").style.display = "block";
      document.getElementById("pageNum").textContent = page;
      currentPage = page;

    } catch (err) {
      alert("Error: " + err.message);
    }
  }

  function nextPage() {
    loadAnalytics(currentPage + 1);
  }

  function prevPage() {
    if (currentPage > 1) {
      loadAnalytics(currentPage - 1);
    }
  }
</script>

</html>