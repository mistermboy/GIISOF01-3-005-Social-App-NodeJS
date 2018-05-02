module.exports = function(app, swig, gestorBD) {

  /*
   * AW_CU_01 GET Controller for the registration end-point.
   */
  app.get("/registrarse", function(req, res) {
    var respuesta = swig.renderFile('views/bregistro.html', {});
    res.send(respuesta);
  });

  /*
   * Controller for the register / login
   */
  app.post('/usuario', function(req, res) {
    if (req.body.password != req.body.password2) {
      res.redirect("/registrarse?mensaje=Las contraseñas no coinciden");
    } else {
      var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
              .update(req.body.password).digest('hex');
      console.log(seguro);

      var criterio = {
        email: req.body.email
      };

      gestorBD.obtenerUsuarios(criterio, function(usuarios) {
        if (usuarios != null || usuarios.length > 0) {
          res.redirect("/registrarse"
                  + "?mensaje=Email ya registrado en el sistema"
                  + "&tipoMensaje=alert-danger ");
        } else {
          var usuario = {
            email: req.body.email,
            name: req.body.nombre,
            password: seguro
          };
          console.log("User to create -> " + usuario.name + " " + usuario.email
                  + " " + usuario.password);

          gestorBD.insertarUsuario(usuario, function(id) {
            if (id == null) {
              res.redirect("/registrarse?mensaje=Error al registrar usuario");
            } else {
              res.redirect("/identificarse?mensaje=Nuevo usuario registrado");
            }
          });
        }
      });
    }
  });

  app.get("/identificarse", function(req, res) {
    var respuesta = swig.renderFile('views/bidentificacion.html', {});
    res.send(respuesta);
  });

  app.post("/identificarse", function(req, res) {
    var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
    var criterio = {
      email: req.body.email,
      password: seguro
    }
    gestorBD.obtenerUsuarios(criterio, function(usuarios) {
      if (usuarios == null || usuarios.length == 0) {
        req.session.usuario = null;
        res.redirect("/identificarse" + "?mensaje=Email o password incorrecto"
                + "&tipoMensaje=alert-danger ");
      } else {
        req.session.usuario = usuarios[0].email;
        res.redirect("/listarUsuarios");
      }
    });
  });

  app.get("/listarUsuarios", function(req, res) {
    var pg = parseInt(req.query.pg); // Es String !!!
    if (req.query.pg == null) { // Puede no venir el param
      pg = 1;
    }

    var usuarios = gestorBD.obtenerUsuarios(pg, function(usuarios,total) {

      var pgUltima = total / 5;
      if (total % 5 > 0) { // Sobran decimales
        pgUltima = pgUltima + 1;
      }

      var respuesta = swig.renderFile('views/blistaUsuarios.html', {
        usuarios: usuarios,
        pgActual: pg,
        pgUltima: pgUltima
      });
      res.send(respuesta);
    });
  });
}
