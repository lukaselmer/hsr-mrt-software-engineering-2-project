require 'test_helper'
require 'rails/performance_test_help'

class Synchronize10TimeEntryTypesTest < ActionDispatch::PerformanceTest
  def setup
    sign_in

    10.times do
      TimeEntryType.create(:description => "Testtype")
    end
  end

  def sign_in
    post_via_redirect "/users/sign_in", "user[email]" => "field_worker@mrt.ch", "user[password]" => "mrt"
    assert_equal "/", path
  end

  def test_synchronize_10_time_entry_types
    post '/time_entry_types/synchronize.json', :format => :json
  end
end
