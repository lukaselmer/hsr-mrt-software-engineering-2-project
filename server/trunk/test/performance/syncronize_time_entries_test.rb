require 'test_helper'
require 'rails/performance_test_help'

class BrowsingTest < ActionDispatch::PerformanceTest
  def test_post_10_time_entries
    sign_in
    post_time_entries 10
  end
  
  def test_post_100_time_entries
    sign_in
    post_time_entries 100
  end
  
  def test_post_1000_time_entries
    sign_in
    post_time_entries 1000
  end

  def sign_in
    post_via_redirect "/users/sign_in", "user[email]" => "field_worker@mrt.ch", "user[password]" => "mrt"
    assert_equal "/", path
  end

  def post_time_entries(times = 1)
    valid_entry = TimeEntry.new(:hashcode => 'h45hc0de', :description => "Valid Entry", :time_start => 3.hours.ago, :time_stop => 1.hour.ago)

    times.times do
      post '/time_entries.json', :time_entry => valid_entry.attributes, :format => :json
      assert_response :success
    end

  end
end
