require 'test_helper'

class TimeEntryTest < ActiveSupport::TestCase

  setup do
    @time_entry = time_entries :one
    @time_entry_2 = time_entries :two
  end

  test "remove_hashcode removes hashcode" do
    assert_not_nil @time_entry.hashcode
    @time_entry.remove_hashcode
    assert_nil @time_entry.hashcode
  end

  test "cannot create time_entry without time_start" do
    invalid_record = TimeEntry.new(:description => "Invalid Entry",:time_stop => 1.hour.ago)
    assert !invalid_record.valid?
  end

  test "cannot create time_entry without time_stop" do
    invalid_record = TimeEntry.new(:description => "Invalid Entry",:time_start => 1.hour.ago)
    assert !invalid_record.valid?
  end

  test "correct duration of time_entry" do
#    TODO Test überarbeiten
    assert_equal Time.at(@time_entry.time_stop - @time_entry.time_start).gmtime, @time_entry.duration
    assert_equal Time.at(@time_entry_2.time_stop - @time_entry_2.time_start).gmtime, @time_entry_2.duration
  end
end
