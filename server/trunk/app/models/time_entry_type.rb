class TimeEntryType < ActiveRecord::Base
  has_many :time_entries
  has_many :time_entry_type_materials
  has_many :materials, :through => :time_entry_type_materials
  belongs_to :time_entry_type
  has_one :time_entry_type
end
