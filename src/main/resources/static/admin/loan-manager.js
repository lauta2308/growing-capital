const { createApp } = Vue


createApp({
    data() {
        return {
            loansInfo: [],
            nombreNuevoPrestamo: "",
            montoPrestamo: "",
            pagosPrestamo: "",
            interesPrestamo: "",


        }
    },
    created() {
        this.loadLoansInfo();


    },
    methods: {
        async loadLoansInfo() {


            await axios.get("/api/loans").then(response => {

                this.loansInfo = response.data;


            })

            .catch(error => console.log(error));
        },

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




        solicitudCrearPrestamo: function() {

            this.nombreNuevoPrestamo = this.nombreNuevoPrestamo.charAt(0).toUpperCase() + this.nombreNuevoPrestamo.slice(1);

            let mensajeErrorForm = document.querySelector(".form-manager-error");
            let nombreDuplicado = false;
            for (let i = 0; i < this.loansInfo.length; i++) {
                if (this.loansInfo[i].name === this.nombreNuevoPrestamo) {
                    nombreDuplicado = true;
                }
            }

            if (this.nombreNuevoPrestamo === "" || this.montoPrestamo === "" || this.pagosPrestamo === "" || this.interesPrestamo === "") {
                mensajeErrorForm.textContent = "Quedan campos sin completar";
                mensajeErrorForm.style.display = "block";

            } else if (nombreDuplicado) {
                mensajeErrorForm.textContent = "Ya existe un préstamo con el mismo nombre";
                mensajeErrorForm.style.display = "block";
            } else if (this.montoPrestamo <= 0) {
                mensajeErrorForm.textContent = "El monto del préstamo no puede ser 0";
                mensajeErrorForm.style.display = "block";
            } else if (this.pagosPrestamo <= 0) {
                mensajeErrorForm.textContent = "Las opciones de pago no pueden ser 0";
                mensajeErrorForm.style.display = "block";
            } else if (this.interesPrestamo <= 0) {
                mensajeErrorForm.textContent = "El interés no puede ser 0";
                mensajeErrorForm.style.display = "block";
            }

            this.agregarNuevoPrestamo();

        },

        agregarNuevoPrestamo: function() {
            axios.post("/admin/newloan", `name=${this.nombreNuevoPrestamo}&maxAmmount=${this.montoPrestamo}&payments=${this.pagosPrestamo}&fee=${this.interesPrestamo}`, {
                    headers: { 'content-type': 'application/x-www-form-urlencoded' }
                })
                .then(Swal.fire(
                    'Excelente!',
                    'Se ha creado un nuevo préstamo.',
                    'success'
                ))
                .then(window.location.href = "../admin/manager.html")

            .catch(error => console.log(error))
        }

    },
    computed: {}
}).mount('#app')