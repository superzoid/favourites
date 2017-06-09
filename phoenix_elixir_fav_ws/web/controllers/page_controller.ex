defmodule PhoenixElixirFavWs.PageController do
  use PhoenixElixirFavWs.Web, :controller

  def index(conn, _params) do
    render conn, "index.html"
  end

    def show(conn, %{"name" => name}) do
    json conn, %{name: "Hello "<> name}
  end
end
