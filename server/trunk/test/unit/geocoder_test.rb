require 'test_helper'

class GeocoderTest < ActiveSupport::TestCase
  # Replace this with your real tests.
  test "valid address translation" do
    res = Geocoder.translate("Hungerbergstrasse 16, 8046 Zuerich")
    assert_nil res[:error]
    assert_in_delta 47.4201566, res[:latitude], 0.00001
    assert_in_delta 8.4998559, res[:longitude], 0.00001
  end

  test "valid address translation with umlauts" do
    res = Geocoder.translate("Hungerbergstrasse 16, 8046 Zürich")
    assert_nil res[:error]
    assert_in_delta 47.4201566, res[:latitude], 0.00001
    assert_in_delta 8.4998559, res[:longitude], 0.00001
  end
  
  test "invalid address translation" do
    res = Geocoder.translate("bubububu")
    assert_not_nil res[:error]
  end
end
