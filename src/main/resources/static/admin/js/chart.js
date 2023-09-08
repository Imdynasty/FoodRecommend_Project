
  var ctx = document.getElementById('myChart').getContext('2d');
  var ctx2 = document.getElementById('myChart2').getContext('2d');
  /** const list = document.querySelector("#chart"); */
  console.log(list);


  var chart1 = new Chart(ctx, {
      type: 'bar', // 그래프의 종류
      data: { //chart에 들어갈 값
          labels: ['한식','양식','일식','중식','분식','카페','패밀리레스토랑','횟집','패스트푸드','뷔페','술집','기타'], // 라벨 값을 대표할 이름
          datasets: [
            {
              label: '남자 맛집 선호도', //범례
              data: list, //실제 데이터값
              backgroundColor: [
                  'rgba(54, 162, 235, 0.2)'
              ],
              borderColor: [
                  'rgba(54, 162, 235, 1)'
              ],
              borderWidth: 1
            },
            {
                label: '여자 맛집 선호도', //범례
                data: list2, //실제 데이터값
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)'
                ],
                    borderWidth: 1
                }
          ]
      },
      options: {
            responsive: false,
            maintainAspectRatio :false,
          scales: {
              y: {
                  beginAtZero: true
              }
          }
      }
  })

  var chart2 = new Chart(ctx2, {
        type: 'bar', // 그래프의 종류
        data: { //chart에 들어갈 값
            labels: ['10대', '20대', '30대', '40대', '50대 이상'], // 라벨 값을 대표할 이름
            datasets: [{
                label: '나이대별 가입자 수', //범례
                data: age, //실제 데이터값
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(31, 255, 176, 0.2)',
                    'rgba(31, 255, 68, 0.2)'

                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(31, 255, 176, 1)',
                    'rgba(31, 255, 68, 1)'

                ],
                borderWidth: 1
            }]
        },
        options: {
                responsive: false,
                maintainAspectRatio :false,
            title: {
                display: true,
                text: '나이대별 가입자'
            },
            legend: {
                display: false
            },
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    })

