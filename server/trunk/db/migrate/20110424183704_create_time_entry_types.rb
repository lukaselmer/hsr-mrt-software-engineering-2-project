class CreateTimeEntryTypes < ActiveRecord::Migration
  def self.up
    create_table :time_entry_types do |t|
      t.text :description
      t.datetime :valid_until
	  t.belongs_to :time_entry_type

      t.timestamps
    end
  end

  def self.down
    drop_table :time_entry_types
  end
end
