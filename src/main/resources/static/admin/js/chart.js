  window.onload = function() {
  const ctx = document.getElementById('myChart');

  const myChart = new Chart(ctx, {
      type: 'bar', // 그래프의 종류
      data: { //chart에 들어갈 값
          labels: ['한식', '양식', '일식'], // 라벨 값을 대표할 이름
          datasets: [{
              label: '방문자 통계', //범례
              data: "${com.foocmend/db/CategorySelect}", //실제 데이터값
              backgroundColor: [
                  'rgba(255, 99, 132, 0.2)',
                  'rgba(54, 162, 235, 0.2)',
                  'rgba(255, 206, 86, 0.2)',
                  'rgba(75, 192, 192, 0.2)'
              ],
              borderColor: [
                  'rgba(255, 99, 132, 1)',
                  'rgba(54, 162, 235, 1)',
                  'rgba(255, 206, 86, 1)',
                  'rgba(75, 192, 192, 1)'
              ],
              borderWidth: 1
          }]
      },
      options: {
          scales: {
              y: {
                  beginAtZero: true
              }
          }
      }
  })
  }