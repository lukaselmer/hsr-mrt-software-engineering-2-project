require 'test_helper'

class TimeEntriesControllerTest < ActionController::TestCase
  include Devise::TestHelpers

  setup do
    @time_entry = time_entries(:one)
    @time_entry_from_other_worker = time_entries(:two)
    login_with_field_worker
  end

  test "should get index" do
    get :index
    assert_response :success
    assert_not_nil assigns(:time_entries)
  end

  test "should get new" do
    get :new
    assert_response :success
  end

  test "should create time_entry" do
    valid_entry = TimeEntry.new(:hashcode => 'h45hc0de', :description => "Valid Entry", :time_start => 3.hours.ago, :time_stop => 1.hour.ago)
    assert_difference('TimeEntry.count') do
      post :create, :time_entry => valid_entry.attributes
    end
    assert_redirected_to time_entry_path(assigns(:time_entry))
  end

  test "should create time_entry by json" do
    valid_entry = TimeEntry.new(:hashcode => 'h45hc0de', :description => "Valid Entry", :time_start => 3.hours.ago, :time_stop => 1.hour.ago)
    assert_difference('TimeEntry.count') do
      post :create, :time_entry => valid_entry.attributes, :format => :json
    end
    assert_response :success
  end

  test "should not create time_entry by json without hashcode" do
    invalid_entry = TimeEntry.new(:description => "Invalid Entry", :time_start => 3.hours.ago, :time_stop => 1.hour.ago)
    assert_no_difference('TimeEntry.count') do
      post :create, :time_entry => invalid_entry.attributes, :format => :json
    end
    assert_response :unprocessable_entity
  end

  test "should remove hashcode" do
    post :remove_hashcode, :id => @time_entry.to_param, :format => :json
    @time_entry.reload
    assert_nil(@time_entry.hashcode)
    assert_response :success
  end

  test "should leave time_entry unchanged if hashcode is already nil" do
    @time_entry.remove_hashcode
    post :remove_hashcode, :id => @time_entry.to_param, :format => :json
    assert_response :success
  end

  test "should not create time_entry by json with existing hashcode" do
    assert_no_difference('TimeEntry.count') do
      post :create, :time_entry => @time_entry.attributes, :format => :json
    end
    assert_response :success
  end

  test "should show time_entry" do
    get :show, :id => @time_entry.to_param
    assert_response :success
  end

  test "should get edit" do
    get :edit, :id => @time_entry.to_param
    assert_response :success
  end

  test "should not get edit from other worker" do
    get :edit, :id => @time_entry_from_other_worker.to_param
    assert_response :forbidden
  end

  test "should update time_entry" do
    put :update, :id => @time_entry.to_param, :time_entry => @time_entry.attributes
    assert_redirected_to time_entry_path(assigns(:time_entry))
  end

  test "should not update time_entry from other worker" do
    put :update, :id => @time_entry_from_other_worker.to_param, :time_entry => @time_entry_from_other_worker.attributes
    assert_response :forbidden
  end

  test "should not update time_entry without start_time" do
    @time_entry.time_start = nil
    put :update, :id => @time_entry.to_param, :time_entry => @time_entry.attributes
    assert_response :success
  end

  test "should not create time_entry without start_time" do
    invalid_entry = TimeEntry.new(:description => "Valid Entry", :time_stop => 1.hour.ago)
    assert_no_difference('TimeEntry.count') do
      post :create, :time_entry => invalid_entry.attributes
    end
    assert_response :success
  end

  test "should destroy time_entry" do
    assert_difference('TimeEntry.count', -1) do
      delete :destroy, :id => @time_entry.to_param
    end
    assert_redirected_to time_entries_path
  end

  test "should destroy time_entry from other worker" do
    assert_no_difference('TimeEntry.count') do
      delete :destroy, :id => @time_entry_from_other_worker.to_param
    end
    assert_response :forbidden
  end
end
