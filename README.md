https://www.figma.com/design/7CvbwmzpuWjC5paFqGieki/Bibliotapp?node-id=0-1&t=7UV0EoNedkUIC6eK-1


Firebase schema do https://dbdiagram.io/d
// Use DBML to define your database structure
// Docs: https://dbml.dbdiagram.io/docs

Table users {
  matricula string [primary key]
  email string
  name string
  password string
  isAdmin bool
  created_at timestamp
}

Table materiais {
  id integer [primary key]
  autor string
  capa binary
  cdu string
  edicao string
  idioma string
  isbn string
  material string
  publicacao string
  titulo string
  created_at timestamp
}

Table exemplares {
  id integer [primary key]
  materiais_id string
  created_at timestamp
}

Table emprestimos {
  id integer [primary key]
  users_matricula string
  exemplares_id string
  emprestado_em timestamp
  devolver_em timestamp
  created_at timestamp
}

Ref: "users"."matricula" < "emprestimos"."users_matricula"

Ref: "exemplares"."id" < "emprestimos"."exemplares_id"

Ref: "materiais"."id" < "exemplares"."materiais_id"