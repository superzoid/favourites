defmodule PhoenixElixirFavWs.PageControllerTest do
  use PhoenixElixirFavWs.ConnCase

#  test "GET /", %{conn: conn} do
#    conn = get conn, "/"
#    assert html_response(conn, 200) =~ "Welcome to Phoenix!"
#  end
#
#  test "stringifys empty map" do
#    result = Helpers.stringify(%{})
#    assert result === "{}"
#  end
#
#  test "stringifys nil" do
#    result = Helpers.stringify(nil)
#    assert result === "nil"
#  end
#
#  test "stringifys empty list" do
#    result = Helpers.stringify([])
#    assert result === "[]"
#  end

  test "stringifys map" do
    m = %{:name => "andy"}
    IO.inspect m
    result = Helpers.stringify(m)
    assert result === "[]"
  end

#  test "stringifys list" do
#        result = Helpers.stringify(["a", "b"])
#        assert result === "["<>quotify("a")<>","<>quotify("b")<>"]"
#  end
  
#  defp quotify(s) do
#    @quote<>s<>@quote
#  end

end
