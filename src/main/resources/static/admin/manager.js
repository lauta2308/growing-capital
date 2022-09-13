  const { createApp } = Vue


  createApp({
      data() {
          return {


          }
      },
      created() {


      },
      methods: {

          abrirMenu: function() {
              let iconoAbrirMenu = document.getElementById("open-menu-icon");
              let iconoCerrarMenu = document.getElementById("close-menu-icon");
              let navegacion = document.getElementById("navegacion");

              iconoAbrirMenu.style.display = "none";
              iconoCerrarMenu.style.display = "block";
              navegacion.style.display = "flex";
          },

          cerrarMenu: function() {
              let iconoAbrirMenu = document.getElementById("open-menu-icon");
              let iconoCerrarMenu = document.getElementById("close-menu-icon");
              let navegacion = document.getElementById("navegacion");

              iconoAbrirMenu.style.display = "block";
              iconoCerrarMenu.style.display = "none";
              navegacion.style.display = "none";

          },
          aparecerLogout: function() {
              Swal.fire({
                  title: '¿Cerrar sesión?',
                  text: "Volverás al index",
                  icon: 'warning',
                  showCancelButton: true,
                  cancelButtonText: "Cancelar",
                  confirmButtonColor: '#3085d6',
                  cancelButtonColor: '#d33',
                  confirmButtonText: 'Si, cerrar sesión'
              }).then((result) => {
                  if (result.isConfirmed) {

                      this.cerrarSesion();
                  }
              })

          },
          ocultarForm: function() {
              var loginFormContainer = document.querySelector(".login-form-container");

              loginFormContainer.style.display = "none";
          },
          cerrarSesion: function() {
              axios.post('/api/logout').then(response => window.location.href = "../index.html")
          },

      },
      computed: {}
  }).mount('#app')